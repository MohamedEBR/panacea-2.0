import React, { useEffect, useState } from 'react'
import { Box, Paper, Typography, CircularProgress, Alert, List, ListItem, ListItemText } from '@mui/material'
import { useAuth } from '../contexts/AuthContext'
import http from '../lib/http'

const Billing = () => {
  const { user, isAuthenticated } = useAuth()
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [records, setRecords] = useState([])

  useEffect(() => {
    const load = async () => {
      try {
        if (!isAuthenticated()) throw new Error('Please sign in')
        // backend endpoint to be implemented later if missing
        const res = await http.get(`/api/v1/members/${user.id}/billing`)
        setRecords(res.data || [])
      } catch (e) {
        setError(e.response?.data?.message || e.message || 'Failed to load billing records')
      } finally {
        setLoading(false)
      }
    }
    if (user?.id) load()
  }, [isAuthenticated, user])

  if (loading) return <Box sx={{ display:'flex', justifyContent:'center', py: 10 }}><CircularProgress /></Box>

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Typography variant="h4" sx={{ mb: 3, fontFamily: '"Saira Semi Condensed"', color: '#9d4f4b' }}>Billing</Typography>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      <Paper sx={{ p: 2 }}>
        {records.length === 0 ? (
          <Typography color="text.secondary">No billing records yet.</Typography>
        ) : (
          <List>
            {records.map((r) => (
              <ListItem key={r.id} divider>
                <ListItemText
                  primary={`$${r.amount} — ${new Date(r.date).toLocaleString()}`}
                  secondary={r.stripeTransactionId ? `Transaction: ${r.stripeTransactionId}` : undefined}
                />
              </ListItem>
            ))}
          </List>
        )}
      </Paper>
    </Box>
  )
}

export default Billing
