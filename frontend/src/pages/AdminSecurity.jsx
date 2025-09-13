import React, { useState } from 'react'
import { Box, Typography, Paper, TextField, Button, Divider, Stack, Alert } from '@mui/material'
import adminService from '../services/adminService'

const AdminSecurity = () => {
  const [email, setEmail] = useState('')
  const [msg, setMsg] = useState('')
  const [error, setError] = useState('')
  const [audit, setAudit] = useState([])
  const [failed, setFailed] = useState([])
  const [blacklisted, setBlacklisted] = useState([])

  const handleUnlock = async () => {
    setMsg(''); setError('')
    try {
      await adminService.unlock(email.trim())
      setMsg(`Unlocked: ${email.trim()}`)
    } catch (e) {
      setError(e.response?.data || e.message)
    }
  }

  const handleCheckLocked = async () => {
    setMsg(''); setError('')
    try {
      const locked = await adminService.isLocked(email.trim())
      setMsg(`${email.trim()} is ${locked ? 'LOCKED' : 'NOT locked'}`)
    } catch (e) {
      setError(e.response?.data || e.message)
    }
  }

  const refreshLogs = async () => {
    setMsg(''); setError('')
    try {
      const [a, f, b] = await Promise.all([
        adminService.getAudit(100),
        adminService.getFailed(100),
        adminService.getBlacklisted(),
      ])
      setAudit(a || []); setFailed(f || []); setBlacklisted(b || [])
    } catch (e) {
      setError(e.response?.data || e.message)
    }
  }

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Typography variant="h4" sx={{ mb: 3, fontFamily: 'Saira Semi Condensed', color: '#9d4f4b' }}>Admin Security</Typography>
      {msg && <Alert sx={{ mb: 2 }} severity="success">{msg}</Alert>}
      {error && <Alert sx={{ mb: 2 }} severity="error">{error}</Alert>}
      <Paper sx={{ p: 2, mb: 3 }}>
        <Stack direction={{ xs: 'column', md: 'row' }} spacing={2} alignItems="center">
          <TextField label="Email" fullWidth value={email} onChange={e => setEmail(e.target.value)} />
          <Button variant="contained" onClick={handleCheckLocked}>Check Locked</Button>
          <Button variant="outlined" onClick={handleUnlock}>Unlock</Button>
        </Stack>
      </Paper>

      <Paper sx={{ p: 2, mb: 3 }}>
        <Stack direction="row" spacing={2}>
          <Button variant="contained" onClick={refreshLogs}>Refresh Logs</Button>
        </Stack>
        <Divider sx={{ my: 2 }} />
        <Typography variant="h6">Audit</Typography>
        <pre style={{ whiteSpace: 'pre-wrap' }}>{JSON.stringify(audit, null, 2)}</pre>
        <Typography variant="h6">Failed Attempts</Typography>
        <pre style={{ whiteSpace: 'pre-wrap' }}>{JSON.stringify(failed, null, 2)}</pre>
        <Typography variant="h6">Blacklisted Tokens</Typography>
        <pre style={{ whiteSpace: 'pre-wrap' }}>{JSON.stringify(blacklisted, null, 2)}</pre>
      </Paper>
    </Box>
  )
}

export default AdminSecurity
