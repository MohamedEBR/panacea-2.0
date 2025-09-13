import http from './http';

const ProgramService = {
  async getPrograms() {
    const res = await http.get('/api/v1/programs');
    return res.data;
  }
};

export default ProgramService;
