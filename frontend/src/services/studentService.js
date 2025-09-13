import http from '../lib/http'

// Student API service
// Endpoints span /api/v1/members/{memberId}/students (create)
// and /api/v1/students/{id} for student-specific operations

const createStudent = async (memberId, data) => {
  const res = await http.post(`/api/v1/members/${memberId}/students`, data)
  return res.data
}

const getStudent = async (id) => {
  const res = await http.get(`/api/v1/students/${id}`)
  return res.data
}

const updateStudent = async (id, data) => {
  const res = await http.put(`/api/v1/students/${id}`, data)
  return res.data
}

const deleteStudent = async (id) => {
  const res = await http.delete(`/api/v1/students/${id}`)
  return res.data
}

const getStudentHistory = async (id) => {
  const res = await http.get(`/api/v1/students/${id}/history`)
  return res.data
}

const addProgram = async (id, programId) => {
  const res = await http.put(`/api/v1/students/${id}/programs`, { programId }, { params: { add: true } })
  return res.data
}

const withdrawProgram = async (id, programId) => {
  const res = await http.put(`/api/v1/students/${id}/programs`, { programId }, { params: { withdraw: true } })
  return res.data
}

const studentService = {
  createStudent,
  getStudent,
  updateStudent,
  deleteStudent,
  getStudentHistory,
  addProgram,
  withdrawProgram,
}

export default studentService
