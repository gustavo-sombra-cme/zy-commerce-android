# ZY-Commerce Android Plan

## Stories & Gherkin

- **ST-01 - Register account**
  As a visitor, I want to create an account with my email and password so that I can access protected product-management features from the Android app.

  ```gherkin
  Scenario: Visitor registers successfully from the sign-up screen
    Given I am on the registration screen
    And I have entered a unique email address
    And I have entered a valid password
    When I tap the "Create account" button
    Then I should see a success confirmation
    And I should be taken to the sign-in screen or signed-in flow
    And I should not need to re-enter the submitted data to continue
  ```

- **ST-02 - Prevent duplicate account registration**
  As a visitor, I want to be told when an email is already in use so that I can recover gracefully instead of retrying blindly.

  ```gherkin
  Scenario: Visitor tries to register with an existing email
    Given I am on the registration screen
    And I have entered an email address that already exists
    And I have entered a valid password
    When I tap the "Create account" button
    Then I should remain on the registration screen
    And I should see an error explaining that the email is already registered
    And the app should let me correct the email or go to sign in
  ```

- **ST-03 - Sign in**
  As a registered user, I want to sign in with my email and password so that I can access my authenticated session and protected actions.

  ```gherkin
  Scenario: Registered user signs in successfully
    Given I am on the sign-in screen
    And I have entered a valid registered email
    And I have entered the correct password
    When I tap the "Sign in" button
    Then I should enter the authenticated area of the app
    And product-management actions should become available
    And my session should remain active until I sign out or the token expires
  ```

- **ST-04 - Show sign-in failure clearly**
  As a registered user, I want clear feedback when my credentials are invalid or my account is inactive so that I understand why access was denied.

  ```gherkin
  Scenario: User enters invalid credentials
    Given I am on the sign-in screen
    And I have entered an unregistered email or incorrect password
    When I tap the "Sign in" button
    Then I should remain on the sign-in screen
    And I should see an error explaining that the credentials are invalid

  Scenario: User tries to sign in with an inactive account
    Given I am on the sign-in screen
    And my account is inactive
    And I have entered the correct email and password
    When I tap the "Sign in" button
    Then I should remain on the sign-in screen
    And I should see an error explaining that the account is inactive
  ```

- **ST-05 - View current profile**
  As a signed-in user, I want to view my basic profile information so that I can confirm which account is active on the device.

  ```gherkin
  Scenario: Signed-in user opens profile
    Given I am signed in
    When I open the profile screen
    Then I should see my email address
    And I should see my user identifier
    And I should not be asked to sign in again if my session is still valid
  ```

- **ST-06 - Browse product catalog**
  As a visitor, I want to view a list of products without signing in so that I can explore the catalog immediately.

  ```gherkin
  Scenario: Visitor opens the catalog for the first time
    Given I am not signed in
    When I open the catalog screen
    Then I should see a list of products if products exist
    And I should be able to access catalog browsing without authentication
    And I should see an empty state if no products are available
  ```

- **ST-07 - Search products**
  As a visitor, I want to search the catalog by SKU or product name so that I can find relevant products faster.

  ```gherkin
  Scenario: Visitor searches products by keyword
    Given I am on the catalog screen
    And the catalog contains matching products
    When I enter a search term in the search field
    And I submit the search
    Then the list should refresh with only matching products
    And each result should match the SKU or product name criteria
  ```

- **ST-08 - Filter products by active status**
  As a visitor, I want to filter the catalog by active or inactive status so that I can narrow the list to the product state I need.

  ```gherkin
  Scenario: Visitor filters to active products only
    Given I am on the catalog screen
    And the catalog contains active and inactive products
    When I select the "Active" filter
    Then I should see only active products in the results
    And the selected filter should remain visible in the UI
  ```

- **ST-09 - Load additional catalog pages**
  As a visitor, I want to load more products when I reach the end of the current results so that I can browse large catalogs without losing context.

  ```gherkin
  Scenario: Visitor loads the next page of products
    Given I am viewing the catalog
    And more than one page of results exists
    When I scroll to the end of the current list or tap "Load more"
    Then the next page of products should be appended or displayed
    And the products already shown should remain visible in order
  ```

- **ST-10 - View product details**
  As a visitor, I want to open a product detail screen so that I can see the full information for one product.

  ```gherkin
  Scenario: Visitor opens product details from the catalog
    Given I am on the catalog screen
    And a product is visible in the list
    When I tap the product
    Then I should see the product name
    And I should see the SKU
    And I should see the description if one exists
    And I should see whether the product is active
    And I should see creation and last update information when available
  ```

- **ST-11 - Create a product**
  As a signed-in user, I want to create a product with the required fields so that the catalog can grow from the Android app.

  ```gherkin
  Scenario: Signed-in user creates a valid product
    Given I am signed in
    And I am on the create-product screen
    And I have entered a valid SKU
    And I have entered a valid product name
    And I may optionally enter a description
    When I tap the "Save product" button
    Then I should see a success confirmation
    And I should be taken to the product detail screen or refreshed catalog list
    And the new product should appear in the catalog
  ```

- **ST-12 - Validate product creation input**
  As a signed-in user, I want immediate feedback for missing or invalid product fields so that I can correct errors before or after submission.

  ```gherkin
  Scenario: Signed-in user submits an invalid product form
    Given I am signed in
    And I am on the create-product screen
    And I have left the SKU or product name empty
    When I tap the "Save product" button
    Then I should remain on the create-product screen
    And I should see field-level or form-level validation messages
    And no product should be created until the errors are fixed
  ```

- **ST-13 - Prevent duplicate SKU creation**
  As a signed-in user, I want to be warned when a SKU already exists so that I do not create conflicting products.

  ```gherkin
  Scenario: Signed-in user tries to create a product with an existing SKU
    Given I am signed in
    And I am on the create-product screen
    And I have entered a SKU already used by another product
    And I have entered the remaining required data
    When I tap the "Save product" button
    Then I should remain on the create-product screen
    And I should see an error explaining that the SKU already exists
    And I should be able to edit the SKU and retry
  ```

- **ST-14 - Update product details**
  As a signed-in user, I want to update a product’s name and description so that product information stays current.

  ```gherkin
  Scenario: Signed-in user updates product details
    Given I am signed in
    And I am viewing a product detail screen
    When I edit the product name or description
    And I save the changes
    Then I should see a success confirmation
    And the updated name and description should be shown on the detail screen
    And the SKU should remain unchanged in the UI
  ```

- **ST-15 - Deactivate a product**
  As a signed-in user, I want to deactivate a product from its detail screen so that it is no longer active without being permanently deleted.

  ```gherkin
  Scenario: Signed-in user deactivates a product
    Given I am signed in
    And I am viewing an active product detail screen
    When I confirm the "Deactivate product" action
    Then I should see a success confirmation
    And the product should be shown as inactive
    And the product should no longer appear in active-only filtered results
  ```

- **ST-16 - Block protected actions for visitors**
  As a visitor, I want protected actions to direct me to sign in so that I understand why I cannot create or modify products yet.

  ```gherkin
  Scenario: Visitor attempts a protected product action
    Given I am not signed in
    And I am on a screen where product creation or editing is available in the navigation
    When I try to start a protected action
    Then I should be redirected to the sign-in flow or shown a sign-in prompt
    And the product action should not proceed until I authenticate
  ```

## Backlog Table

| ID | Story | Epic | Priority | Estimate | Dependencies |
|---|---|---|---|---|---|
| ST-01 | Visitor can register a new account | Authentication | High | 5 SP no AI / 3 SP with AI | None |
| ST-02 | Visitor sees duplicate-email feedback during registration | Authentication | Medium | 2 SP no AI / 1 SP with AI | ST-01 |
| ST-03 | Registered user can sign in and enter authenticated app flow | Authentication | High | 5 SP no AI / 3 SP with AI | ST-01 |
| ST-04 | User sees clear errors for invalid credentials or inactive account | Authentication | High | 3 SP no AI / 2 SP with AI | ST-03 |
| ST-05 | Signed-in user can view their basic profile | Authentication | Medium | 3 SP no AI / 2 SP with AI | ST-03 |
| ST-06 | Visitor can browse the public product catalog | Catalog Browsing | High | 5 SP no AI / 3 SP with AI | None |
| ST-07 | Visitor can search products by SKU or name | Catalog Browsing | High | 3 SP no AI / 2 SP with AI | ST-06 |
| ST-08 | Visitor can filter products by active status | Catalog Browsing | Medium | 3 SP no AI / 2 SP with AI | ST-06 |
| ST-09 | Visitor can load additional pages of product results | Catalog Browsing | Medium | 3 SP no AI / 2 SP with AI | ST-06 |
| ST-10 | Visitor can open a product detail screen | Catalog Browsing | High | 3 SP no AI / 2 SP with AI | ST-06 |
| ST-11 | Signed-in user can create a product | Product Management | High | 5 SP no AI / 3 SP with AI | ST-03, ST-16 |
| ST-12 | Signed-in user sees validation feedback while creating a product | Product Management | High | 3 SP no AI / 2 SP with AI | ST-11 |
| ST-13 | Signed-in user sees duplicate-SKU feedback during product creation | Product Management | Medium | 2 SP no AI / 1 SP with AI | ST-11 |
| ST-14 | Signed-in user can update product name and description | Product Management | High | 5 SP no AI / 3 SP with AI | ST-03, ST-10, ST-16 |
| ST-15 | Signed-in user can deactivate a product | Product Management | Medium | 3 SP no AI / 2 SP with AI | ST-03, ST-10, ST-16 |
| ST-16 | Visitor is blocked from protected product actions until signed in | Access Control & UX | High | 3 SP no AI / 2 SP with AI | ST-03 |

## Sprint Plan

### Sprint 1

- Establish app foundation for public and authenticated navigation flows.
- Deliver `ST-01` Register account.
- Deliver `ST-02` Duplicate-email registration feedback.
- Deliver `ST-03` Sign in.
- Deliver `ST-04` Sign-in error handling for invalid credentials and inactive accounts.
- Deliver `ST-05` Current user profile screen.
- Deliver `ST-06` Public catalog browsing.
- Deliver `ST-10` Product detail screen.
- Deliver `ST-16` Visitor protection and sign-in gating for protected actions.

### Sprint 2

- Deliver `ST-07` Product search.
- Deliver `ST-08` Active/inactive product filtering.
- Deliver `ST-09` Pagination / load more behavior.
- Deliver `ST-11` Create product.
- Deliver `ST-12` Product form validation feedback.
- Deliver `ST-13` Duplicate-SKU create-product handling.
- Deliver `ST-14` Update product details.
- Deliver `ST-15` Deactivate product.

## Risks & Assumptions

- Assumption: the Android app is allowed to expose product-management features, even though the backend currently has no roles or permissions and any authenticated user can perform write actions.
- Assumption: session persistence on Android will store the bearer token securely and treat token expiration as a sign-in requirement because refresh tokens are out of scope.
- Assumption: product images, pricing, stock, cart, checkout, and orders are intentionally excluded from this Android slice.
- Assumption: the current profile feature only needs to show user identifier and email because no richer customer profile exists yet.
- Risk: the backend Swagger contract is not perfectly aligned with runtime behavior, so Android error handling should be based on tested responses rather than generated models alone.
- Risk: there is no role model yet, which may create a future mismatch if the business later decides product creation/editing should be admin-only.
- Risk: the backend currently supports deactivation but not reactivation, so the Android UX for inactive products may need redesign later.
- Risk: API error payloads are not fully uniform across all endpoints, so UI error mapping may require endpoint-specific handling.
- Risk: search and pagination performance characteristics are still unknown on real production-sized catalogs.
- Risk: token expiration and re-authentication flow may feel abrupt to users until refresh-token support exists.
