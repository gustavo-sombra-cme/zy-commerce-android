# ZY-Commerce Initial Requirements (Current MVP Slice)

## Summary
- Build an e-commerce platform foundation with user access and product catalog management.
- Current implemented scope covers account access and catalog product administration/browsing.

## MVP Scope
- User accounts: register account, log in, retrieve current signed-in user.
- Authentication: use a session token to access protected operations.
- Product catalog browsing: list products, search products, filter by active status, paginate results, view a single product.
- Product management: create product, update product details, deactivate product.

## User Account Requirements
- A visitor can create an account with email and password.
- Email must be unique per account.
- A registered user can sign in with email and password.
- An inactive user cannot sign in.
- A signed-in user can retrieve their own basic profile.
- Current profile response includes only user identifier and email.

## Product Catalog Requirements
- A visitor can browse the product catalog without signing in.
- A visitor can search products by SKU or product name.
- A visitor can filter products by active/inactive status.
- Product list supports pagination.
- A visitor can open a product detail view.
- Product detail includes identifier, SKU, name, description, active status, creation date, and last update date.

## Product Administration Requirements
- Only authenticated users can create products.
- Only authenticated users can update product details.
- Only authenticated users can deactivate products.
- Creating a product requires SKU and name.
- Product description is optional.
- SKU must be unique across products.
- Updating a product can change only name and description.
- Deactivating a product removes it from active use without hard deletion.

## Product Data Rules
- Product SKU is required.
- Product name is required.
- Product description is optional.
- Product must keep an active/inactive status.
- Product must keep creation timestamp.
- Product must keep last-updated timestamp when modified.

## Access Rules
- Product read operations are public.
- Product write operations require authentication.
- Profile retrieval requires authentication.

## Current Out Of Scope
- Customer profiles beyond basic identity
- Roles and permissions
- Refresh tokens / long-lived sessions
- Shopping cart
- Checkout
- Orders
- Payments
- Inventory management
- Product reactivation
- Product images, categories, pricing, stock, reviews, or shipping logic
