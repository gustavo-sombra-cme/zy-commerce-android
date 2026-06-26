# PRODUCT — ZY-Commerce Android App (MVP)

This document is the source of truth for product scope and product boundaries in the Android repository.

## Product Summary

ZY-Commerce Android is the mobile client for the ZY-Commerce platform. The current MVP is designed to provide a simple but usable mobile experience for user access and product catalog management, backed by the existing ZY-Commerce backend.

This MVP is intentionally narrow. It focuses on authentication, catalog browsing, and basic product administration workflows that are already supported by the backend. It does not yet attempt to cover the full e-commerce lifecycle.

## Product Purpose

The product exists to:

- give users a mobile entry point into the ZY-Commerce platform
- validate the Android architecture, delivery flow, and backend integration early
- support public product discovery
- support authenticated product-management actions from mobile
- create a foundation that can grow later into a broader commerce application

## Intended Users

### Visitor

A visitor is a user who has not signed in.

Visitors can:

- browse the product catalog
- search products
- filter products by active status
- paginate through product results
- open product details
- register for an account
- move into the sign-in flow when a protected action is attempted

### Authenticated User

An authenticated user is a user who has signed in successfully.

Authenticated users can:

- access their current profile
- create products
- update product details
- deactivate products
- continue using all public catalog features

## Current MVP Scope

The current MVP includes the following business capabilities.

### 1. Account Access

The app supports basic account access flows:

- register a new account with email and password
- sign in with email and password
- restore a previously persisted session when valid
- retrieve the current signed-in user profile

The current profile scope is intentionally small:

- user identifier
- email address

### 2. Product Catalog Browsing

The app supports public product browsing:

- open a catalog list without signing in
- search products by SKU or product name
- filter products by active or inactive status
- paginate through product results
- open a product detail screen

The product detail view currently includes:

- product identifier
- SKU
- name
- description
- active/inactive status
- creation timestamp
- last updated timestamp when available

### 3. Product Management

The app supports authenticated product-management actions:

- create a product
- update product name and description
- deactivate a product

Current business rules from the backend that shape this product:

- only authenticated users may perform product write actions
- product creation requires SKU and name
- description is optional
- SKU must be unique
- product update changes only name and description
- deactivation is a soft business action, not permanent deletion

## Feature Overview

### Registration

The registration feature allows a visitor to create an account using email and password.

Expected behavior:

- valid registration succeeds and moves the user forward
- duplicate email is handled gracefully with a readable message
- the user should be able to retry without losing context

### Sign In

The sign-in feature creates an authenticated session for a registered user.

Expected behavior:

- valid credentials open the authenticated experience
- invalid credentials show a clear error
- inactive account state is surfaced clearly
- session should remain usable until sign-out or token expiration

### Profile

The profile feature is a lightweight identity check.

Expected behavior:

- show who is currently signed in
- allow the user to confirm the active account on the device

### Catalog List

The catalog list is the main entry point for public browsing.

Expected behavior:

- accessible without authentication
- show available products
- show empty state when no products exist

### Search

Search helps users find products faster.

Expected behavior:

- search by SKU
- search by product name
- refresh visible results to match the query

### Filter

Filter helps users narrow product results by status.

Expected behavior:

- allow active-only view
- allow inactive-only view
- preserve visible filter context in the screen

### Pagination

Pagination supports larger catalogs without overwhelming the screen.

Expected behavior:

- load additional results when requested
- preserve already loaded context

### Product Detail

The detail screen gives full visibility into a selected product.

Expected behavior:

- open from catalog list
- display all currently supported product fields
- become the launch point for edit/deactivate actions when allowed

### Create Product

Authenticated users can add products from mobile.

Expected behavior:

- collect required product fields
- validate obvious input problems
- show success or conflict feedback clearly

### Update Product

Authenticated users can maintain product information.

Expected behavior:

- edit only supported fields
- preserve SKU as immutable in the mobile workflow
- reflect updated values after save

### Deactivate Product

Authenticated users can deactivate a product.

Expected behavior:

- require explicit user confirmation
- update product state visibly
- affect active-only filtered views correctly

### Protected Action Gating

Visitors should never be confused about why they cannot perform write actions.

Expected behavior:

- protected product-management actions must route the user into sign-in
- unsigned users should receive a clear prompt instead of silent failure

## Product Boundaries

This MVP is not yet a full commerce application.

Out of scope today:

- shopping cart
- checkout
- orders
- payments
- inventory management
- pricing logic
- product images
- categories
- reviews
- shipping flows
- roles and permissions
- refresh-token session strategy
- customer profile data beyond basic identity
- product reactivation

## Key Product Assumptions

- Any authenticated user can currently manage products because the backend has no role model yet.
- The Android app is intentionally aligned to the backend’s current behavior, not a future full-commerce vision.
- Session expiration may currently require re-authentication because refresh tokens are out of scope.
- Product-management functionality is included in this MVP because the backend already supports it, even if that later evolves into a more admin-specific workflow.

## Success Criteria for the MVP

The MVP should be considered successful when:

- a visitor can register and sign in successfully from Android
- a signed-in user can confirm their active identity
- a visitor can browse, search, filter, and inspect products
- an authenticated user can create, update, and deactivate products from the app
- protected flows behave clearly and safely for unsigned users
- the app provides a stable foundation for future commerce features

## Relationship to Other Product Documents

This document explains what the product is and what it is meant to do.

Related documents:

- `requirements/mvp_requirements_ecommerce.md` defines the current MVP requirements
- `requirements/ecommerce-android-plan.md` defines the stories, backlog, sprint plan, and risks
- `ARCHITECTURE.MD` defines how the app should be built technically
- root-level `feature_list.json` defines machine-readable implementation scope for agent-driven execution
