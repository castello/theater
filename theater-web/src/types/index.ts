export interface Movie {
  id: number;
  title: string;
  description: string;
  durationMinutes: number;
  rating: string;
  posterUrl: string;
  releaseDate: string;
  status: 'showing' | 'upcoming' | 'ended';
}

export interface Theater {
  id: number;
  name: string;
  location: string;
  address: string;
  screens?: Screen[];
}

export interface Screen {
  id: number;
  name: string;
  totalSeats: number;
  theaterId: number;
  theaterName: string;
}

export interface Seat {
  id: number;
  rowLabel: string;
  seatNumber: number;
  seatType: 'standard' | 'premium' | 'wheelchair';
  seatCode: string;
  available: boolean;
}

export interface Showtime {
  id: number;
  movieId: number;
  movieTitle: string;
  screenId: number;
  screenName: string;
  theaterId: number;
  theaterName: string;
  startTime: string;
  endTime: string;
  price: number;
}

export interface Reservation {
  id: number;
  userId: number;
  showtimeId: number;
  movieTitle: string;
  theaterName: string;
  screenName: string;
  showStartTime: string;
  totalPrice: number;
  status: 'pending' | 'confirmed' | 'cancelled';
  createdAt: string;
  seatCodes: string[];
  payment?: Payment;
}

export interface Payment {
  id: number;
  reservationId: number;
  amount: number;
  paymentMethod: 'card' | 'kakao' | 'naver';
  status: 'pending' | 'completed' | 'refunded';
  paidAt: string;
}

export interface ReservationRequest {
  userId: number;
  showtimeId: number;
  seatIds: number[];
}

export interface PaymentRequest {
  reservationId: number;
  paymentMethod: string;
}
