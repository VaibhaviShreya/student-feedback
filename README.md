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

### Default admin account
- username: `admin`
- password: `admin123`

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

## API endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/courses`
- `POST /api/courses` (admin only)
- `DELETE /api/courses/{id}` (admin only)
- `GET /api/feedback`
- `POST /api/feedback`
