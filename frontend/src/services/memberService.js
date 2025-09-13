import http from '../lib/http'

// Member API service
// All endpoints are under /api/v1/members
// Note: Backend currently expects memberId in path and enforces access via DataOwnershipService

const getMemberProfile = async (memberId) => {
  const res = await http.get(`/api/v1/members/${memberId}`)
  return res.data
}

const updateMemberProfile = async (memberId, data) => {
  const res = await http.put(`/api/v1/members/${memberId}/info`, data)
  return res.data
}

const updatePassword = async (memberId, data) => {
  const res = await http.put(`/api/v1/members/${memberId}/password`, data)
  return res.data
}

const getMemberStudents = async (memberId) => {
  const res = await http.get(`/api/v1/members/${memberId}/students`)
  return res.data
}

const freezeMember = async (memberId) => {
  const res = await http.put(`/api/v1/members/${memberId}/freeze`)
  return res.data
}

const unfreezeMember = async (memberId) => {
  const res = await http.put(`/api/v1/members/${memberId}/unfreeze`)
  return res.data
}

const memberService = {
  getMemberProfile,
  updateMemberProfile,
  updatePassword,
  getMemberStudents,
  freezeMember,
  unfreezeMember,
}

export default memberService
