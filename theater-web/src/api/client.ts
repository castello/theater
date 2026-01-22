import axios from 'axios';
import type {
  Movie,
  Theater,
  Showtime,
  Seat,
  Reservation,
  Payment,
  ReservationRequest,
  PaymentRequest,
} from '../types';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Movies
export const movieApi = {
  getAll: () => api.get<Movie[]>('/movies').then((res) => res.data),
  getById: (id: number) => api.get<Movie>(`/movies/${id}`).then((res) => res.data),
  getShowing: () => api.get<Movie[]>('/movies/showing').then((res) => res.data),
  getUpcoming: () => api.get<Movie[]>('/movies/upcoming').then((res) => res.data),
  search: (keyword: string) =>
    api.get<Movie[]>('/movies/search', { params: { keyword } }).then((res) => res.data),
};

// Theaters
export const theaterApi = {
  getAll: () => api.get<Theater[]>('/theaters').then((res) => res.data),
  getById: (id: number) => api.get<Theater>(`/theaters/${id}`).then((res) => res.data),
};

// Showtimes
export const showtimeApi = {
  getById: (id: number) => api.get<Showtime>(`/showtimes/${id}`).then((res) => res.data),
  getByMovie: (movieId: number, date: string) =>
    api.get<Showtime[]>(`/showtimes/movie/${movieId}`, { params: { date } }).then((res) => res.data),
  getByTheater: (theaterId: number, date: string) =>
    api.get<Showtime[]>(`/showtimes/theater/${theaterId}`, { params: { date } }).then((res) => res.data),
  getSeats: (showtimeId: number) =>
    api.get<Seat[]>(`/showtimes/${showtimeId}/seats`).then((res) => res.data),
};

// Reservations
export const reservationApi = {
  create: (data: ReservationRequest) =>
    api.post<Reservation>('/reservations', data).then((res) => res.data),
  getById: (id: number) => api.get<Reservation>(`/reservations/${id}`).then((res) => res.data),
  getByUser: (userId: number) =>
    api.get<Reservation[]>(`/reservations/user/${userId}`).then((res) => res.data),
  cancel: (id: number) =>
    api.patch<Reservation>(`/reservations/${id}/cancel`).then((res) => res.data),
};

// Payments
export const paymentApi = {
  process: (data: PaymentRequest) => api.post<Payment>('/payments', data).then((res) => res.data),
  getByReservation: (reservationId: number) =>
    api.get<Payment>(`/payments/reservation/${reservationId}`).then((res) => res.data),
  refund: (reservationId: number) =>
    api.post<Payment>(`/payments/reservation/${reservationId}/refund`).then((res) => res.data),
};
