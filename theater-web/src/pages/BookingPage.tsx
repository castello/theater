import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { showtimeApi, reservationApi, paymentApi } from '../api/client';
import SeatSelector from '../components/SeatSelector';
import Loading from '../components/Loading';
import type { Showtime, Seat } from '../types';

const MOCK_USER_ID = 1; // 실제로는 로그인 시스템에서 가져옴

export default function BookingPage() {
  const { showtimeId } = useParams<{ showtimeId: string }>();
  const navigate = useNavigate();

  const [showtime, setShowtime] = useState<Showtime | null>(null);
  const [seats, setSeats] = useState<Seat[]>([]);
  const [selectedSeats, setSelectedSeats] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const [step, setStep] = useState<'seats' | 'payment' | 'complete'>('seats');
  const [paymentMethod, setPaymentMethod] = useState<string>('card');
  const [processing, setProcessing] = useState(false);
  const [reservationId, setReservationId] = useState<number | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [showtimeData, seatsData] = await Promise.all([
          showtimeApi.getById(Number(showtimeId)),
          showtimeApi.getSeats(Number(showtimeId)),
        ]);
        setShowtime(showtimeData);
        setSeats(seatsData);
      } catch (error) {
        console.error('Failed to fetch data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [showtimeId]);

  const handleSeatClick = (seatId: number) => {
    setSelectedSeats((prev) =>
      prev.includes(seatId) ? prev.filter((id) => id !== seatId) : [...prev, seatId]
    );
  };

  const getSelectedSeatCodes = () => {
    return selectedSeats
      .map((id) => seats.find((s) => s.id === id)?.seatCode)
      .filter(Boolean)
      .join(', ');
  };

  const totalPrice = showtime ? showtime.price * selectedSeats.length : 0;

  const handleProceedToPayment = async () => {
    if (selectedSeats.length === 0) {
      alert('좌석을 선택해주세요.');
      return;
    }

    setProcessing(true);
    try {
      const reservation = await reservationApi.create({
        userId: MOCK_USER_ID,
        showtimeId: Number(showtimeId),
        seatIds: selectedSeats,
      });
      setReservationId(reservation.id);
      setStep('payment');
    } catch (error) {
      alert('예약 생성에 실패했습니다.');
      console.error(error);
    } finally {
      setProcessing(false);
    }
  };

  const handlePayment = async () => {
    if (!reservationId) return;

    setProcessing(true);
    try {
      await paymentApi.process({
        reservationId,
        paymentMethod,
      });
      setStep('complete');
    } catch (error) {
      alert('결제에 실패했습니다.');
      console.error(error);
    } finally {
      setProcessing(false);
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

  if (loading) return <Loading />;
  if (!showtime) return <div className="container" style={{ paddingTop: '100px' }}>상영 정보를 찾을 수 없습니다.</div>;

  return (
    <div style={{ paddingTop: '80px' }}>
      <div className="container">
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 350px', gap: '40px', padding: '40px 0' }}>
          {/* Main Content */}
          <div>
            {step === 'seats' && (
              <>
                <h2 className="section-title">좌석 선택</h2>
                <SeatSelector
                  seats={seats}
                  selectedSeats={selectedSeats}
                  onSeatClick={handleSeatClick}
                />
              </>
            )}

            {step === 'payment' && (
              <>
                <h2 className="section-title">결제 수단 선택</h2>
                <div className="payment-methods">
                  {[
                    { id: 'card', name: '신용카드' },
                    { id: 'kakao', name: '카카오페이' },
                    { id: 'naver', name: '네이버페이' },
                  ].map((method) => (
                    <div
                      key={method.id}
                      className={`payment-method ${paymentMethod === method.id ? 'selected' : ''}`}
                      onClick={() => setPaymentMethod(method.id)}
                    >
                      <div className="payment-method-name">{method.name}</div>
                    </div>
                  ))}
                </div>
              </>
            )}

            {step === 'complete' && (
              <div style={{ textAlign: 'center', padding: '60px 0' }}>
                <div style={{ fontSize: '60px', marginBottom: '20px' }}>&#10003;</div>
                <h2 style={{ fontSize: '28px', marginBottom: '15px' }}>예매가 완료되었습니다!</h2>
                <p style={{ color: 'var(--text-secondary)', marginBottom: '30px' }}>
                  예매 내역에서 확인하실 수 있습니다.
                </p>
                <button className="btn btn-primary" onClick={() => navigate('/my-reservations')}>
                  예매 내역 확인
                </button>
              </div>
            )}
          </div>

          {/* Summary Sidebar */}
          <div>
            <div className="summary-card">
              <h3 style={{ marginBottom: '20px' }}>예매 정보</h3>

              <div className="summary-row">
                <span className="summary-label">영화</span>
                <span className="summary-value">{showtime.movieTitle}</span>
              </div>

              <div className="summary-row">
                <span className="summary-label">극장</span>
                <span className="summary-value">{showtime.theaterName}</span>
              </div>

              <div className="summary-row">
                <span className="summary-label">상영관</span>
                <span className="summary-value">{showtime.screenName}</span>
              </div>

              <div className="summary-row">
                <span className="summary-label">상영일시</span>
                <span className="summary-value">{formatDateTime(showtime.startTime)}</span>
              </div>

              <div className="summary-row">
                <span className="summary-label">선택 좌석</span>
                <span className="summary-value">
                  {selectedSeats.length > 0 ? getSelectedSeatCodes() : '-'}
                </span>
              </div>

              <div className="summary-row">
                <span className="summary-label">인원</span>
                <span className="summary-value">{selectedSeats.length}명</span>
              </div>

              <div className="summary-row">
                <span className="summary-label">총 금액</span>
                <span className="summary-value summary-total">
                  {totalPrice.toLocaleString()}원
                </span>
              </div>

              {step === 'seats' && (
                <button
                  className="btn btn-primary"
                  style={{ width: '100%', marginTop: '20px' }}
                  onClick={handleProceedToPayment}
                  disabled={selectedSeats.length === 0 || processing}
                >
                  {processing ? '처리 중...' : '결제하기'}
                </button>
              )}

              {step === 'payment' && (
                <div style={{ marginTop: '20px' }}>
                  <button
                    className="btn btn-primary"
                    style={{ width: '100%', marginBottom: '10px' }}
                    onClick={handlePayment}
                    disabled={processing}
                  >
                    {processing ? '결제 중...' : `${totalPrice.toLocaleString()}원 결제`}
                  </button>
                  <button
                    className="btn btn-secondary"
                    style={{ width: '100%' }}
                    onClick={() => setStep('seats')}
                    disabled={processing}
                  >
                    이전으로
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
