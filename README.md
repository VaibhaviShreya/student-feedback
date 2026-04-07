# Student Feedback & Course Management

A proper full-stack app using:
- **Frontend:** React + Vite
- **Backend:** Spring Boot + Spring Security + JWT
- **Database:** H2 (in-memory)

After login, the frontend shows courses and feedback, and students can submit feedback.

## Folder structure
- `backend/` Spring Boot API
- `frontend/` React website

## Backend setup
```bash
cd backend
mvn spring-boot:run
```
Backend runs at: `http://localhost:8080`

### Default accounts
- admin: `admin` / `admin123`
- teacher: `teacher` / `teacher123`

## Frontend setup
```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```
Website runs at: `http://localhost:5173`

## Main flow
1. Open `http://localhost:5173`
2. Login/Register
3. On success, dashboard loads:
   - course list
   - feedback list
   - feedback submission form


## Run in GitHub Codespaces
1. Push this repository to GitHub.
2. Open the repo in **Code** -> **Codespaces** -> **Create codespace on main**.
3. Wait for the container setup (`postCreateCommand` installs frontend dependencies).
4. Start both services from terminal:
   ```bash
   ./scripts/codespaces-run.sh
   ```
   or in separate terminals:
   ```bash
   cd backend && mvn spring-boot:run
   cd frontend && npm run dev -- --host 0.0.0.0
   ```
5. Open forwarded ports:
   - `5173` -> React UI
   - `8080` -> Spring API


### Codespaces troubleshooting
- If you get `No such file or directory` for scripts, run:
  ```bash
  git pull
  chmod +x scripts/*.sh
  ```
- If you get `cd: backend: No such file or directory`, you are already inside `backend/`. Use:
  ```bash
  mvn spring-boot:run
  ```
  or from repo root:
  ```bash
  ./scripts/run-backend.sh
  ```
- If you get `mvn: command not found`, run backend using wrapper script (it checks for `mvnw` or `mvn` and shows a clear error):
  ```bash
  ./scripts/run-backend.sh
  ```

- If your terminal is inside `backend/`, use:
  ```bash
  ./scripts/codespaces-run.sh
  ```
  (This wrapper moves to repo root and runs the main script.)
- If backend fails with `Unrecognized option: --enable-native-access=ALL-UNNAMED`, run backend with:
  ```bash
  ./scripts/run-backend.sh
  ```
  This script removes that JVM flag from environment variables before starting Spring Boot.
- If backend startup shows `403 Forbidden` while downloading Maven artifacts, that is a network/registry access issue in your environment (not API auth). You must allow access to Maven Central or configure a Maven proxy/mirror.

## CORS
Backend CORS is configured for local frontend development origins (`localhost` / `127.0.0.1` on any port) and permits preflight `OPTIONS` requests.

## API endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/courses`
- `POST /api/courses` (admin only)
- `DELETE /api/courses/{id}` (admin only)
- `GET /api/feedback`
- `POST /api/feedback`
