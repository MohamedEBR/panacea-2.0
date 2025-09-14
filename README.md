# Panacea Karate Academy

A full-stack membership, billing, and program management platform for martial arts schools. Built with Spring Boot (Java), React (Vite), Stripe, and PostgreSQL.

## Features

- **User Registration & Login**: JWT-based authentication, password reset, account lockout, admin unlock.
- **Member Dashboard**: Add/manage students, enroll in programs, view billing, update profile.
- **Multi-step Signup**: Register family, select programs, eligibility gating, Stripe payment.
- **Stripe Integration**: Checkout, webhook activation, monthly billing, receipts via email.
- **Admin Tools**: Unlock accounts, view audit logs, manage users/programs.
- **Blog**: Public blog with admin-only write access.
- **Security**: CORS, XSS, password policy, token revocation, audit logging.
- **Email**: Welcome, password reset, and billing receipt emails.

## Tech Stack

- **Backend**: Spring Boot 3, Spring Security (JWT), JPA/Hibernate, PostgreSQL, Stripe SDK, JavaMailSender
- **Frontend**: React 18 (Vite), MUI, React Router v6, Axios, Toastify

## Local Development

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL

### Backend Setup
1. Copy `.env.example` to `.env` and set DB, JWT, Stripe, and mail credentials.
2. Edit `src/main/resources/application.properties` as needed.
3. Build and run:
   ```powershell
   mvn -q -DskipTests package
   mvn spring-boot:run
   ```

### Frontend Setup
1. In `frontend/`, install dependencies:
   ```powershell
   npm install
   npm run dev
   ```
2. App runs at [http://localhost:5173](http://localhost:5173)

### Common Ports
- Backend: 8080
- Frontend: 5173

## Environment Variables
- See `.env.example` for required variables (DB, JWT, Stripe, mail).

## CORS
- Configured for `localhost` and `127.0.0.1` on common dev ports. Adjust in `SecurityConfiguration.java` if needed.

## Email Setup
- For Gmail: enable 2FA and use an App Password.
- For other SMTP: update `application.properties`.

## Stripe
- Use test keys for development.
- Test card: `4242 4242 4242 4242` (any future expiry, any CVC, any ZIP).

## Database
- Default: PostgreSQL
- Schema auto-updates (`spring.jpa.hibernate.ddl-auto=update`)

## Troubleshooting
- **CORS errors**: Check allowed origins in backend config.
- **Email not sent**: Check SMTP credentials and logs for `[DEV ONLY] Password reset link...`.
- **DB resets**: Ensure `ddl-auto=update` (not `create-drop`).

## License
MIT

---

For questions or contributions, open an issue or PR on GitHub.
