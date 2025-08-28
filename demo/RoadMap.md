
# 🥋 Panacea Karate Academy Backend Development Roadmap

## ✅ Phase 1: Core CRUD and Validation (Data Integrity First)

- [X] PUT: Member profile edit (name, address, phone, etc.)
- [X] Implement Member status field (ACTIVE/SUSPENDED/CANCELLED/FROZEN)
- [X] Auto-suspend members with zero students
- [X] Student CRUD: Add/Edit/Withdraw students from a member
- [X] Enforce max 5 programs per student
- [X] Add `yearsInClub` field to Student model
- [X] Validate student belt, age, and experience for program enrollment
- [X] Enforce program space limits (NoProgramSpaceException)
- [X] Implement missing custom exceptions (MemberNotFoundException, StudentNotFoundException, etc.)
- [X] Global exception handling improvements
- [X] Add @Transactional where needed

## ✅ Phase 2: Stripe Integration and Billing Logic

- [X] Integrate Stripe API for customer creation and payment
- [X] Save Stripe customer ID in Member entity
- [X] Monthly billing scheduler (runs 1st of each month)
- [X] Calculate charges based on student program enrollments
- [X] Charge members via Stripe
- [X] Store receipts in billingHistory
- [ ] Email payment receipts
- [X] Allow members to update payment methods (Stripe)
- [ ] Enforce future-effective program changes (affect next month only)
- [X] Implement Freeze/Unfreeze logic with billing rules

## ✅ Phase 3: Password and Security Flow

- [X] PUT: Member change password (authenticated)
- [X] POST: Forgot Password → generate reset token
- [X] POST: Reset Password (token-based)
- [X] Email password reset links
- [X] Handle token expiry and invalidation (InvalidTokenException)

## ✅ Phase 4: Super User Admin Panel Features

- [ ] View all members (with filtering and pagination)
- [ ] Suspend/reactivate members manually
- [ ] View billing history for any member
- [ ] Export reports (member lists, billing summaries, etc.)
- [ ] CRUD endpoints for Programs
- [ ] Create abstract `Media` class with fields: id, title, content, createdAt
- [ ] Create subclasses: Blog, Achievement, Event, PaidEvent
- [ ] CRUD endpoints for Blogs, Achievements, Events, PaidEvents
- [ ] Role-protect all admin endpoints (SUPER_USER only)

## ✅ Phase 5: Email Notifications + Optional Extras

- [ ] Email notifications for:
    - Payment receipts
    - Password resets
    - Membership status changes (freeze, unfreeze, suspension)
- [ ] Optional improvements:
    - Soft deletes
    - Admin dashboard for reports
    - Activity logging / audit trail
    - Rate limiting (security hardening)
    - Email verification after registration (optional)
