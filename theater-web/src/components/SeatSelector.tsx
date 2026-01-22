import { useMemo } from 'react';
import type { Seat } from '../types';

interface Props {
  seats: Seat[];
  selectedSeats: number[];
  onSeatClick: (seatId: number) => void;
}

export default function SeatSelector({ seats, selectedSeats, onSeatClick }: Props) {
  const seatsByRow = useMemo(() => {
    const grouped: Record<string, Seat[]> = {};
    seats.forEach((seat) => {
      if (!grouped[seat.rowLabel]) {
        grouped[seat.rowLabel] = [];
      }
      grouped[seat.rowLabel].push(seat);
    });
    Object.keys(grouped).forEach((row) => {
      grouped[row].sort((a, b) => a.seatNumber - b.seatNumber);
    });
    return grouped;
  }, [seats]);

  const rows = Object.keys(seatsByRow).sort();

  return (
    <div>
      <div className="screen" />
      <p className="screen-label">SCREEN</p>

      <div className="seats-container">
        {rows.map((row) => (
          <div key={row} className="seat-row">
            <span className="seat-row-label">{row}</span>
            {seatsByRow[row].map((seat) => {
              const isSelected = selectedSeats.includes(seat.id);
              const isPremium = seat.seatType === 'premium';

              return (
                <button
                  key={seat.id}
                  className={`seat ${seat.available ? 'available' : 'reserved'} ${
                    isSelected ? 'selected' : ''
                  } ${isPremium ? 'premium' : ''}`}
                  onClick={() => seat.available && onSeatClick(seat.id)}
                  disabled={!seat.available}
                  title={`${seat.seatCode} ${isPremium ? '(프리미엄)' : ''}`}
                >
                  {seat.seatNumber}
                </button>
              );
            })}
            <span className="seat-row-label">{row}</span>
          </div>
        ))}
      </div>

      <div className="seat-legend">
        <div className="seat-legend-item">
          <div
            className="seat-legend-box"
            style={{ background: 'var(--bg-card)', border: '1px solid var(--border-color)' }}
          />
          <span>선택가능</span>
        </div>
        <div className="seat-legend-item">
          <div className="seat-legend-box" style={{ background: 'var(--primary)' }} />
          <span>선택됨</span>
        </div>
        <div className="seat-legend-item">
          <div className="seat-legend-box" style={{ background: '#333' }} />
          <span>예약됨</span>
        </div>
        <div className="seat-legend-item">
          <div
            className="seat-legend-box"
            style={{ background: 'var(--bg-card)', border: '2px solid var(--warning)' }}
          />
          <span>프리미엄</span>
        </div>
      </div>
    </div>
  );
}
