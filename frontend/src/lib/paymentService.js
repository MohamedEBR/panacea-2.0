import http from './http';

const PaymentService = {
  async createSubscription(payload) {
    const res = await http.post('/api/stripe/create-subscription', { data: payload });
    return res.data; // This is the URL to redirect to
  }
};

export default PaymentService;
