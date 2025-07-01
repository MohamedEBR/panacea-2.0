
# 🥋 Panacea Karate Academy Backend Development Roadmap

## ✅ Phase 1: Core CRUD and Validation (Data Integrity First)

- [ ] PUT: Member profile edit (name, address, phone, etc.)
- [ ] Implement Member status field (ACTIVE/SUSPENDED/CANCELLED/FROZEN)
- [ ] Auto-suspend members with zero students
- [ ] Student CRUD: Add/Edit/Withdraw students from a member
- [ ] Enforce max 5 programs per student
- [ ] Add `yearsInClub` field to Student model
- [ ] Validate student belt, age, and experience for program enrollment
- [ ] Enforce program space limits (NoProgramSpaceException)
- [ ] Implement missing custom exceptions (MemberNotFoundException, StudentNotFoundException, etc.)
- [ ] Global exception handling improvements
- [ ] Add @Transactional where needed

## ✅ Phase 2: Stripe Integration and Billing Logic

- [ ] Integrate Stripe API for customer creation and payment
- [ ] Save Stripe customer ID in Member entity
- [ ] Monthly billing scheduler (runs 1st of each month)
- [ ] Calculate charges based on student program enrollments
- [ ] Charge members via Stripe
- [ ] Store receipts in billingHistory
- [ ] Email payment receipts
- [ ] Allow members to update payment methods (Stripe)
- [ ] Enforce future-effective program changes (affect next month only)
- [ ] Implement Freeze/Unfreeze logic with billing rules

## ✅ Phase 3: Password and Security Flow

- [ ] PUT: Member change password (authenticated)
- [ ] POST: Forgot Password → generate reset token
- [ ] POST: Reset Password (token-based)
- [ ] Email password reset links
- [ ] Handle token expiry and invalidation (InvalidTokenException)

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
