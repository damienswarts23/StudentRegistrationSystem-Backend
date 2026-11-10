# Security and administrator profile update

## Start here

1. Open the backend and frontend Maven projects in IntelliJ, using JDK 17 or newer. Reload Maven dependencies.
2. Start MySQL. Keep the existing `student_registration_db` and the database connection configured in the backend's `application.properties`. No database deletion or manual reset is required. Hibernate `ddl-auto=update` adds the `app_users` table.
3. Run `StudentRegistrationSystemBackendApplication` first, then `StudentRegistrationSystemFrontend`.
4. Sign in with the initial admin below. This account is inserted on the backend's first successful startup only if its username does not already exist.

| Field | Initial value |
| --- | --- |
| Email / username | admin@cput.ac.za |
| Password | Admin@2026! |
| Name | Damien Swarts |
| Role | ADMIN |

The account has not been inserted into your local MySQL database by this delivery; startup creates it. An existing account's password and profile are never reset on restart.

Click the gear beside the employee photo to open settings. Save your name, upload a PNG/JPEG photo (maximum 2 MB), or change your password. The actual account name replaces the old system title. Until a photo is uploaded, a neutral silhouette appears. Photos and names persist in MySQL and belong to the signed-in account. A password change signs out the current session.

Change the initial password in settings. For a different bootstrap account, set `APP_ADMIN_USERNAME`, `APP_ADMIN_PASSWORD`, and `APP_ADMIN_NAME` before starting the backend. The default credentials are for the local classroom demonstration.

## Lecturer's approach

This implementation follows the supplied `Loginsecurity.zip` example and `Security Notes For AppDev3.pdf`, particularly the roles, Spring Security and method-security sections and chapter exercise. It retains:

- A JPA `AppUser`, role enum and user repository.
- `CustomUserDetailsService` returning Spring Security users and role authorities.
- BCrypt password hashing and `PasswordEncoder` verification.
- A `SecurityFilterChain`, form login, server sessions, role checks and logout invalidation.
- A startup initializer that creates the first admin only when absent.
- Friendly login, permission and validation messages.

The lecturer's sample renders browser templates. This project keeps its existing manually coded Swing GUI, so the client submits the login form to `/api/auth/login`, keeps the session cookie, and fetches its profile from `/api/auth/me`. It sends Spring's CSRF token with requests. Successful login returns a status code rather than redirecting Swing to an HTML dashboard. Roles are ADMIN, STUDENT and LECTURER to match this application's users. `@EnableMethodSecurity` and `@PreAuthorize` enforce role and record ownership checks on backend controller methods.

Lecture videos were not transcribed or independently verified in this change; the supplied notes and runnable login example are the implementation references. This is not a claim that every verbal assessment instruction has been checked.

## Roles and account creation

| Role | Allowed work |
| --- | --- |
| ADMIN | Manage students, lecturers, departments, courses and classes; edit own profile; create other admin accounts; view shared admin activity. |
| STUDENT | Sign in, read own application result and personal details, update own permitted personal fields, register for classes and remove own registrations. |
| LECTURER | Sign in, read own lecturer details/classes and view students in assigned classes. |
| Not signed in | Browse the course catalogue and submit a new application. Application creation/result access is restricted to the applicant created in that same session. |

Settings now contains **Create admin account** only. Enter an email, full name and password, then confirm the password. The server also rejects STUDENT or LECTURER creation through this endpoint. Existing student and lecturer accounts still work; their records continue to be managed from the dashboard. Creating a record does not create a login account. Lecturer credentials are now created with Save Lecturer. Students set their login password on the accepted application results page while their application session is active. Admins can enable existing student accounts from the student details page.

## Shared admin log

The dashboard includes an **Admin log** tab backed by the `admin_activity_log` database table. Hibernate adds this table on startup. Every admin using the same backend/database can view the latest 200 successful admin write requests, newest first, with the actor's name/email, South African date/time, action and record/action path. It loads on dashboard opening and tab selection; Refresh fetches newer activity. Failed requests and read-only viewing are not logged. No passwords, photos, request bodies or query parameters are recorded. There is no UI or endpoint to edit/delete log entries. History begins after this update; previous changes cannot be reconstructed. This is a request-level activity history, not a field-by-field before/after diff. Cascade operations appear as their initiating request.

The admin heading is now 40-point bold (previously 23-point).

The existing tracking-by-email screen now requires an authorized login or the same newly created application session; knowing someone else's email no longer gives access to their result. Reopening the app clears the anonymous application session. Returning applicants need administrator assistance; a separate applicant account/recovery workflow is outside this admin-profile update.

## Files to inspect for the presentation

Backend:
- `Config/SecurityConfig.java`: form login, sessions, CSRF and method security.
- `Config/DataInitializer.java`: first administrator account.
- `Domain/AppUser.java`, `Domain/Role.java`, `Repository/AppUserRepository.java`.
- `Service/CustomUserDetailsService.java`: database authentication and BCrypt-backed credentials.
- `Security/AccountAccess.java`: student, lecturer and application ownership checks.
- `Controller/AccountController.java`: current account, name/photo/password and account creation.
- Existing controllers: default admin restrictions and explicit role/ownership exceptions.

Frontend:
- `service/BackendApiClient.java`: login, cookies, CSRF, profile and readable failures.
- `view/LoginGUI.java`: authenticated role-based navigation.
- `view/AdminDashboardGUI.java`: account name, gear and employee photo.
- `view/AdminSettingsGUI.java`, `view/AdminPhotoPanel.java`, `view/SettingsIcon.java`.

## Validation performed

- All frontend production Java files compiled successfully with the JDK 17 compiler.
- `BackendApiClientSessionTest` passed against a local mock HTTP server: form login, cookies, CSRF rotation, profile retrieval/update, URL encoding, 403 messages, logout and failed login cleanup. This verifies client transport behavior, not the actual Spring server.
- Syntax parsing passed for all project Java source files.
- Added `AccountSecurityTest` for actual Spring authentication, hashed passwords, role denials, ownership, CSRF, profile isolation, photos and logout. Original CRUD controller tests now authenticate as an admin and supply CSRF tokens.
- Backend compilation and Spring integration tests could not run here because Maven Central DNS resolution failed. The uploaded backend's original Spring Boot 4.1.0 dependency version was preserved. No backend runtime, MySQL migration or full desktop visual check is claimed.

On your machine, run the focused backend test from `StudentRegistrationSystem-Backend`:

```bat
mvnw.cmd -Dtest=AccountSecurityTest test
```

It uses an isolated H2 database. The original full test suite still uses its configured MySQL test database.

For the frontend transport test, run `BackendApiClientSessionTest.main` in IntelliJ with both `src/main/java` and `src/test/java` on the classpath (JDK 17+). It starts and stops its own localhost test server and does not connect to your database.

## Local versus remote use

The default API is `http://localhost:8080/api`. HTTP is limited to loopback by the client. For a remote backend, configure a valid HTTPS deployment and set the frontend VM option `-Dsrs.api.url=https://your-host/api`; do not disable certificate verification. Set secure session cookies on that deployment. TLS deployment and certificates are not bundled.

Passwords are never sent in query strings and never returned by profile endpoints. Photos are decoded, checked for type/dimensions and rewritten as PNG before storage. No invented employee photo or personal account data is supplied.

Latest checks: frontend production and transport test compile/run; Java syntax parsing. Backend integration tests include admin-only account creation and shared activity logging, but remain unexecuted here because Maven Central is inaccessible.

## Lecturer login fix

Use both updated projects and restart the backend. Saving a new lecturer now asks for an initial password and creates the lecturer record plus a BCrypt-backed LECTURER account in one database transaction. Use the generated lecturer email and the password entered at save time. The account links to the lecturer person ID, not their lecturer ID. Settings remains admin-account creation only.

For a lecturer created before this fix: Admin dashboard → Lecturers → All Lecturers → open their details → Enable lecturer login. Choose and confirm a password, then sign out and sign in with the displayed lecturer email and that password. This does not duplicate or delete the lecturer. An existing login is never overwritten. If it reports that a login already exists, this action does not reset that password.

The lecturer create API now takes {"lecturer": {...}, "initialPassword": "..."} on both POST /api/lecturers and POST /api/lecturers/create-text. Passwords are not included in the lecturer response, stored as plaintext or recorded in the admin log. The existing-record action is POST /api/lecturers/{personId}/login-account, admin-only with CSRF protection.

Validation: frontend compilation and transport tests passed; Java syntax parsing passed. Added focused service tests for linked BCrypt credentials, existing-record activation, duplicate-account protection and weak passwords, plus an API role-denial test. Backend tests cannot run here because Maven dependencies cannot be downloaded.

## Student login and tracking correction

Use both updated projects, then restart the backend and frontend. For existing students, sign in as admin → Students → View All Students → open student details → Enable student login. Set a password with 10+ characters, uppercase, lowercase and a number. Sign in using the generated student email and this password, NOT the student number. No student record or class registrations are removed. Existing account passwords are never overwritten.

For new accepted applicants, the application results page has Set login password. This works only for the applicant created in the current session (or an authenticated admin); knowing another student's ID/email cannot activate their login. If that session has ended, an admin must enable the existing student's login. The endpoint retains CSRF and record ownership checks. Settings remains admin-account creation only.

The tracking screen no longer turns every exception into 'No processed application'. Access/session failures explain the sign-in or admin activation step, a genuine 404 reports no result, and connection failures use the existing backend error display. The old student-number-as-password instructions have been removed. Viewing results again after leaving the application session requires signing in with the enabled student account.

Validation: frontend compilation and transport checks passed, Java syntax checks passed. Added student credential/hash, duplicate protection and ownership tests. Backend tests remain unexecuted because Maven dependencies are unavailable in this environment.

## Student and lecturer profile/settings update

Both dashboards now show the signed-in name in 40-point bold, the account's profile photo and a settings gear. Settings includes My profile (photo upload/removal), Personal details (personal email, telephone and address), and Password. Student Edit My Details was removed from the dashboard; editing is now inside settings. Lecturer self-editing derives the record from the authenticated principal and cannot change another lecturer or academic fields. Photos are always written to the authenticated account. Admin-account creation stays admin-only.

For the existing student sign-in problem: admin → Students → View All Students → open the student → Set/reset student login. Confirm, choose a new password, then sign out and use the generated student email shown by the success message plus that password. This creates missing credentials or replaces the password only for the correctly linked STUDENT account. It preserves the student record and registrations and never overwrites an unrelated account. It does not revoke other sessions already signed in. A screenshot alone cannot determine which credentials are currently in your local database.

Login errors now distinguish a rejected sign-in from a successful authentication followed by failure to load the linked record. The student number is not the login password unless deliberately chosen (and it does not satisfy the new password rules).

Added backend tests for matching-account reset, cross-account protection and student photo isolation. Frontend compilation/transport tests and Java syntax parsing pass. Full backend build, database execution and desktop visual checks remain unverified in this environment.

## UI polish

Shared styling now uses compact 38-pixel form controls, restrained navy surfaces, blue navigation, green create/save/confirm actions, amber edits/password changes and red delete/remove/deregister actions. Neutral controls remain white with an outline. Buttons have rounded corners, hover/pressed/disabled states and a visible keyboard focus outline. Text and labels still identify the action, so colour is not the only cue.

Dashboard action cards use separate vertical rows for title, description and button. Form grids no longer stretch inputs vertically. Shared window scrolling preserves content when the window is too short, instead of forcing cards to overlap. Tables, tabs, fonts, borders, card padding and form margins were updated across the frontend. Business rules and backend code were not changed in this UI pass.

Validation: compiled all frontend Java sources; transport checks passed; rendered shared components headlessly and checked input height and dashboard card overlap. The actual Windows desktop appearance and every populated screen still need a local visual check.


## RegiLearn application theme

The frontend uses navy #1E293B, indigo #4F46E5, pale surface #EEF2FF and success green #22C55E. Destructive actions remain red. Fonts use Inter or Segoe UI when available, with a system fallback.

All application pages launch maximized. The outer full-page scrollbar has been removed; action cards have a compact height, long label/value forms use two columns, and input heights stay bounded. Tables and variable-length class lists retain their own scrolling so records remain accessible.

The splash shows only the animated cap on a transparent window on supported desktops. It pulses for 3.2 seconds, then fades into the first page over 650 milliseconds. Click or press Escape to skip. Unsupported desktops fall back to a shaped or opaque window. This is a startup animation, not a backend connection indicator. Pages use a brief fade through the application background, without movement. Set -Dregilearn.reduceMotion=true to disable the pulse and page motion.

Action cards and text buttons use the bundled Lucide icon family consistently. Original SVGs, vector geometry and LUCIDE-LICENSE.txt are included; no icon download is needed at runtime. The decorative academic illustrations have been removed. Existing user-uploaded profile photos remain supported.

Replace the complete frontend project, including src/main/resources, then run StudentRegistrationSystemFrontend. No database changes or account recreation are needed.

Validation: all frontend Java sources compiled; mock session transport tests passed. Compact card bounds and a 13-field form were checked at three widths, icons were loaded from bundled resources, and headless previews were inspected. Full interactive Windows animation and display-scaling checks still require the target computer.


### Splash, photography and transition corrections

The splash no longer takes foreground priority after creating Home. Once the splash closes, Home is explicitly raised and focused, with a short Windows foreground-priority pulse that is then released. Page transitions use a 320 ms fade through the application background and discard old page snapshots on completion. Real library photos are bundled for Home and Login (see PHOTO-CREDITS.md). Photo scaling is cached during animation. Lucide icons now render directly as vector geometry at native display resolution, rather than as resized PNGs.

Validation: frontend compilation, session transport tests, photo resource loading, vector rendering at 300% scale and animation timer progress/completion passed. Headless visual previews were inspected. Windows foreground behavior still needs verification on the user's machine; it cannot be fully exercised by the headless environment.


### Revised page transition

Navigation now uses a 320 ms fade through the application surface. The outgoing page fades first, followed by the incoming page; text from different screens does not overlap, and the layout stays stationary. The startup logo animation is unchanged. Reduced-motion mode still shows pages immediately.


### Consistent button palette

Everyday primary actions (create, add, view, edit, save, assign and confirm) and their card icons use the brand indigo. Destructive actions retain red. Logout uses a red outline and red icon/text with a pale red hover state. Secondary navigation remains neutral. Green is reserved for success indicators rather than ordinary action buttons.


### Admin dashboard profile area

The admin dashboard now has a larger portrait (up to 120 x 140 logical pixels), a 72-pixel settings button and a 38-pixel vector settings icon. Both controls are vertically centered. The taller profile row moves the tabs and cards down into the previously unused space; the portrait reduces on shorter displays to preserve room for the cards.


### Consistent role dashboards and settings

Admin, lecturer and student dashboards share DashboardComponents for portrait sizing, the settings button and its placement. Each dashboard uses the same menu header, spacing and tabbed action area. Lecturer and student dashboards show class actions first and retain their account/academic information in a My details tab. Settings use consistently left-aligned fields/actions and the shared portrait size; a reserved name-action row keeps the photo position consistent while preserving admin-only name editing. Permissions and backend behavior are unchanged. Frontend compilation and session tests passed.
