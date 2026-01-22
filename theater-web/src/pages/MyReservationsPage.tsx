import { useEffect, useState } from 'react';
import { reservationApi } from '../api/client';
import Loading from '../components/Loading';
import type { Reservation } from '../types';

const MOCK_USER_ID = 1;

export default function MyReservationsPage() {
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        const data = await reservationApi.getByUser(MOCK_USER_ID);
        setReservations(data);
      } catch (error) {
        console.error('Failed to fetch reservations:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchReservations();
  }, []);

  const handleCancel = async (id: number) => {
    if (!confirm('예매를 취소하시겠습니까?')) return;

    try {
      await reservationApi.cancel(id);
      setReservations((prev) =>
        prev.map((r) => (r.id === id ? { ...r, status: 'cancelled' as const } : r))
      );
    } catch (error) {
      alert('취소에 실패했습니다.');
      console.error(error);
    }
  };

  const formatDateTime = (dateString: string) => {
    return new Date(dateString).toLocaleString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'confirmed':
        return '예매 완료';
      case 'pending':
        return '결제 대기';
      case 'cancelled':
        return '취소됨';
      default:
        return status;
    }
  };

  if (loading) return <Loading />;

  return (
    <div style={{ paddingTop: '80px' }}>
      <section className="section">
        <div className="container">
          <h2 className="section-title">예매 내역</h2>

          {reservations.length > 0 ? (
            reservations.map((reservation) => (
              <div key={reservation.id} className="reservation-card">
                <div className="reservation-header">
                  <h3 className="reservation-title">{reservation.movieTitle}</h3>
                  <span className={`reservation-status status-${reservation.status}`}>
                    {getStatusLabel(reservation.status)}
                  </span>
                </div>

                <div className="reservation-info">
                  <div>
                    <span style={{ color: 'var(--text-secondary)' }}>극장: </span>
                    {reservation.theaterName} {reservation.screenName}
                  </div>
                  <div>
                    <span style={{ color: 'var(--text-secondary)' }}>상영일시: </span>
                    {formatDateTime(reservation.showStartTime)}
                  </div>
                  <div>
                    <span style={{ color: 'var(--text-secondary)' }}>좌석: </span>
                    {reservation.seatCodes.join(', ')}
                  </div>
                  <div>
                    <span style={{ color: 'var(--text-secondary)' }}>결제금액: </span>
                    {reservation.totalPrice.toLocaleString()}원
                  </div>
                </div>

                {reservation.status === 'confirmed' && (
                  <div style={{ marginTop: '15px' }}>
                    <button
                      className="btn btn-secondary"
                      onClick={() => handleCancel(reservation.id)}
                    >
                      예매 취소
                    </button>
                  </div>
                )}
              </div>
            ))
          ) : (
            <p style={{ color: 'var(--text-secondary)' }}>예매 내역이 없습니다.</p>
          )}
        </div>
      </section>
    </div>
  );
}
