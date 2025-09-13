import http from '../lib/http'

const unlock = async (email) => {
  const res = await http.post(`/api/v1/admin/security/unlock`, null, { params: { email } })
  return res.data
}

const isLocked = async (email) => {
  const res = await http.get(`/api/v1/admin/security/locked`, { params: { email } })
  return res.data
}

const getAudit = async (limit = 100) => {
  const res = await http.get(`/api/v1/admin/security/audit`, { params: { limit } })
  return res.data
}

const getFailed = async (limit = 100) => {
  const res = await http.get(`/api/v1/admin/security/failed`, { params: { limit } })
  return res.data
}

const getBlacklisted = async () => {
  const res = await http.get(`/api/v1/admin/security/blacklisted`)
  return res.data
}

export default { unlock, isLocked, getAudit, getFailed, getBlacklisted }
