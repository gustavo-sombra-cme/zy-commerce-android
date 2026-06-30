#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

usage() {
  cat <<'USAGE'
Usage:
  scripts/close-cycle.sh --approved --feature ST-04 --summary "..." \
    --verification "./gradlew :feature:auth:test" \
    --note "Manual backend smoke skipped; targeted tests covered this pass."

Required:
  --approved          Confirms the human approved the Evaluator pass and closure.
  --feature ID        Feature ID to mark done.
  --summary TEXT      Completion summary for feature_list.json, HISTORY, and handoff.

Optional:
  --verification TEXT Add a verification command or result. Repeatable.
  --note TEXT         Add a completion note or residual risk. Repeatable.
  --next-step TEXT    Override the recommended next step in SESSION_HANDOFF.MD.
USAGE
}

ruby -rjson -rdate -e '
args = ARGV.dup
approved = false
feature_id = nil
summary = nil
verification = []
notes = []
next_step_override = nil

until args.empty?
  arg = args.shift
  case arg
  when "--approved"
    approved = true
  when "--feature"
    feature_id = args.shift
  when "--summary"
    summary = args.shift
  when "--verification"
    verification << args.shift
  when "--note"
    notes << args.shift
  when "--next-step"
    next_step_override = args.shift
  when "--help", "-h"
    exit 2
  else
    warn "Unknown argument: #{arg}"
    exit 2
  end
end

unless approved && feature_id && summary
  warn "Missing required --approved, --feature, or --summary."
  exit 2
end

today = Date.today.iso8601
feature_path = "feature_list.json"
progress_path = ".harness/docs/PROGRESS.MD"
history_path = ".harness/docs/HISTORY.MD"
handoff_path = ".harness/docs/SESSION_HANDOFF.MD"
manifest_path = ".harness/harness_manifest.json"

feature_data = JSON.parse(File.read(feature_path))
features = feature_data.fetch("features")
feature = features.find { |item| item.fetch("id") == feature_id }
unless feature
  warn "Feature not found in feature_list.json: #{feature_id}"
  exit 1
end

unless feature.fetch("status") == "in_progress"
  warn "Feature #{feature_id} must be in_progress before closure. Current status: #{feature.fetch("status")}"
  exit 1
end

feature["status"] = "done"
feature["implementation"] = {
  "completed_on" => today,
  "summary" => summary,
  "verification" => verification,
  "notes" => notes
}
feature_data["last_updated"] = today

File.write(feature_path, JSON.pretty_generate(feature_data) + "\n")

next_feature = features.find { |item| item.fetch("status") == "planned" }
next_label = next_feature ? "#{next_feature.fetch("id")} #{next_feature.fetch("name")}" : "None currently planned"
next_step = next_step_override || (next_feature ? "Plan and implement `#{next_feature.fetch("id")}` #{next_feature.fetch("name")}." : "Review backlog and choose the next planned feature.")

progress_template = <<~PROGRESS
  # PROGRESS.MD

  This file is for the active feature or active harness task only.

  When the active work is complete:

  1. get an Evaluator `pass` decision and explicit human approval for closure
  2. keep the durable completion evidence in `feature_list.json`
  3. run `scripts/close-cycle.sh --approved ...` so history, handoff, progress, and runtime outputs are updated together

  ## Active Item

  - Feature or task ID:
  - Feature or task name:
  - Status:
  - Started on:
  - Last updated on:

  ## Scope

  - In scope:
  - Out of scope:

  ## Progress Summary

  - Completed in this active item:
  - Remaining work:
  - Blockers or risks:

  ## Verification

  - Commands run:
  - Results:
  - Skipped checks:

  ## Handoff Notes

  - Next recommended step:
  - Files or modules likely to change next:
  - Notes for the next session:
PROGRESS

history = File.read(history_path)
verification_lines = verification.empty? ? ["- Not recorded."] : verification.map { |item| "- `#{item}`" }
note_lines = notes.empty? ? ["- None."] : notes.map { |item| "- #{item}" }
archive = <<~ARCHIVE
  ### #{today} — #{feature_id} #{feature.fetch("name")}

  Completed:

  - #{summary}

  Validation:

  #{verification_lines.join("\n")}

  Notes:

  #{note_lines.join("\n")}

  Next feature:

  - #{next_label}

ARCHIVE

if history.include?("## Archived History\n\n")
  history = history.sub("## Archived History\n\n", "## Archived History\n\n#{archive}")
else
  history = history + "\n\n#{archive}"
end

handoff_verification_lines = verification.empty? ? ["- Not recorded."] : verification.map { |item| "- `#{item}`" }
handoff_note_lines = notes.empty? ? ["- No residual risks recorded."] : notes.map { |item| "- #{item}" }
handoff = <<~HANDOFF
  # SESSION_HANDOFF.MD

  This file is a short restart note for the next session only.

  Keep it compact. Put durable completion history in `.harness/docs/HISTORY.MD` in newest-first order, active-item detail in `.harness/docs/PROGRESS.MD`, and feature state plus evidence in `feature_list.json`.

  ## Current Objective

  - #{next_step}

  ## Current Status

  - `#{feature_id}` accepted and closed out; ready for the next feature.

  ## Active Feature

  - None.

  ## Next Planned Feature

  - #{next_label}

  ## What Changed Most Recently

  - #{summary}
  - Ran approved cycle closure to update `feature_list.json`, `.harness/docs/HISTORY.MD`, `.harness/docs/PROGRESS.MD`, and `.harness/docs/SESSION_HANDOFF.MD`.
  - Cleared runtime agent outputs after approved Evaluator closure.

  ## Verification Snapshot

  #{handoff_verification_lines.join("\n")}

  ## Blockers / Risks

  #{handoff_note_lines.join("\n")}

  ## Recommended Next Step

  - #{next_step}

  ## Files Likely Needed Next

  - `feature_list.json`
  - `.harness/docs/PROGRESS.MD`
  - `.harness/docs/HISTORY.MD`
  - `.harness/docs/SESSION_HANDOFF.MD`
HANDOFF

File.write(progress_path, progress_template)
File.write(history_path, history)
File.write(handoff_path, handoff)

manifest = JSON.parse(File.read(manifest_path))
manifest.fetch("runtime_agent_outputs").each do |path|
  File.write(path, "") if File.exist?(path)
end

puts "Closed #{feature_id}: #{feature.fetch("name")}"
puts "Next planned feature: #{next_label}"
puts "Updated feature_list.json, HISTORY.MD, SESSION_HANDOFF.MD, PROGRESS.MD, and runtime agent outputs."
' -- "$@" || {
  status=$?
  if [[ "$status" -eq 2 ]]; then
    usage >&2
  fi
  exit "$status"
}
