# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Theater Reservation System - a full-stack application with Java/Spring Boot backend and React/TypeScript frontend.

## Build & Run Commands

### Database (MySQL)
```bash
mysql -u root -p < sample_data/init_database.sql
```

### Backend (theater-api)
```bash
cd theater-api
./mvnw spring-boot:run        # Runs on http://localhost:8081
./mvnw clean package          # Build JAR
```

### Frontend (theater-web)
```bash
cd theater-web
npm install                   # First time only
npm run dev                   # Runs on http://localhost:3001
npm run build                 # Production build
```

## Architecture

```
theater-web (React)  →  /api proxy  →  theater-api (Spring Boot)  →  MySQL
    :3001                                    :8081                   :3306
```

### Backend Structure (theater-api)
- **Controllers**: REST endpoints at `/api/{movies,theaters,showtimes,reservations,payments}`
- **Services**: Business logic with `@Transactional` for complex operations
- **Repositories**: JPA interfaces with custom queries
- **Entities**: Movie, Theater, Screen, Seat, Showtime, Reservation, ReservedSeat, Payment, User

### Frontend Structure (theater-web)
- **Pages**: HomePage, MoviesPage, MovieDetailPage, TheatersPage, BookingPage, MyReservationsPage
- **Components**: Header, MovieCard, SeatSelector, Loading
- **API Client**: `src/api/client.ts` - Axios-based API wrapper

## Key Configuration

### API Proxy (vite.config.ts)
Frontend proxies `/api` requests to backend at `http://localhost:8081`

### Database (application.yml)
- Database: `theater`
- Credentials: `root / password`
- DDL mode: `validate` (expects schema from init_database.sql)

### Ports
- Backend: 8081
- Frontend: 3001
- MySQL: 3306

## Reservation Flow

1. User selects showtime → BookingPage
2. Seat selection → `POST /api/reservations` (creates pending reservation)
3. Payment method → `POST /api/payments` (processes payment, confirms reservation)
4. View history → MyReservationsPage with cancel option

## Notes

- Frontend uses `MOCK_USER_ID = 1` in BookingPage.tsx (no auth system implemented)
- Seat types: standard, premium, wheelchair
- Payment methods: card, kakao, naver
- Reservation statuses: pending, confirmed, cancelled
