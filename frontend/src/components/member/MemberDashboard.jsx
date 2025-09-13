import React, { useEffect, useMemo, useState } from 'react'
import { Box, Typography, Grid, Paper, Chip, Button, List, ListItem, ListItemText, Divider, CircularProgress, Alert } from '@mui/material'
import { useAuth } from '../../contexts/AuthContext'
import memberService from '../../services/memberService'
import { Link, useNavigate } from 'react-router-dom'

const StatusChip = ({ status }) => {
  const color = useMemo(() => {
    switch ((status || '').toUpperCase()) {
      case 'ACTIVE': return 'success'
      case 'FROZEN': return 'default'
      case 'SUSPENDED': return 'warning'
      case 'CANCELLED': return 'error'
      default: return 'info'
    }
  }, [status])
  return <Chip label={status || 'UNKNOWN'} color={color} size="small" />
}

const MemberDashboard = () => {
  const { user, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [member, setMember] = useState(null)
  const [students, setStudents] = useState([])

  useEffect(() => {
    const load = async () => {
      try {
        if (!isAuthenticated()) {
          navigate('/login')
          return
        }
        // assuming user.id exists in AuthContext user object
        const memberId = user?.id
        if (!memberId) throw new Error('Member id not found in session')

        const [m, s] = await Promise.all([
          memberService.getMemberProfile(memberId),
          memberService.getMemberStudents(memberId),
        ])
        setMember(m)
        setStudents(s || [])
      } catch (e) {
        console.error(e)
        setError(e.response?.data?.message || e.message || 'Failed to load dashboard')
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [isAuthenticated, navigate, user])

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}>
        <CircularProgress />
      </Box>
    )
  }

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Typography variant="h4" sx={{ mb: 3, fontFamily: '"Saira Semi Condensed"', color: '#9d4f4b' }}>
        Member Dashboard
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>
      )}

      <Grid container spacing={3}>
        {/* Profile summary */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" sx={{ mb: 1 }}>Profile</Typography>
            <Divider sx={{ mb: 2 }} />
            <Typography><b>Name:</b> {member?.name} {member?.lastName}</Typography>
            <Typography><b>Email:</b> {member?.email}</Typography>
            <Typography><b>Phone:</b> {member?.phone || '—'}</Typography>
            <Typography><b>Address:</b> {member?.address || '—'}, {member?.city || ''} {member?.postalCode || ''}</Typography>
            <Typography sx={{ mt: 1 }}><b>Status:</b> <StatusChip status={member?.status} /></Typography>
            <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
              <Button component={Link} to="/profile" variant="outlined">Edit Profile</Button>
              <Button component={Link} to="/students" variant="outlined">Manage Students</Button>
              <Button component={Link} to="/billing" variant="outlined">Billing</Button>
            </Box>
          </Paper>
        </Grid>

        {/* Students */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" sx={{ mb: 1 }}>Students</Typography>
            <Divider sx={{ mb: 2 }} />
            {students.length === 0 ? (
              <Typography color="text.secondary">No students yet. Add your first student.</Typography>
            ) : (
              <List>
                {students.map(st => (
                  <ListItem key={st.id} disablePadding sx={{ mb: 1 }}>
                    <ListItemText
                      primary={<>
                        <b>{st.name}</b>{' '}
                        {st.status && <StatusChip status={st.status} />}
                      </>}
                      secondary={
                        st.programs && st.programs.length > 0
                          ? `Programs: ${st.programs.map(p => p.name).join(', ')}`
                          : 'No enrolled programs'
                      }
                    />
                  </ListItem>
                ))}
              </List>
            )}
            <Box sx={{ mt: 2 }}>
              <Button component={Link} to="/students/create" variant="contained" sx={{ backgroundColor: '#9d4f4b' }}>
                Add Student
              </Button>
            </Box>
          </Paper>
        </Grid>

        {/* Billing snapshot */}
        <Grid item xs={12}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" sx={{ mb: 1 }}>Billing Snapshot</Typography>
            <Divider sx={{ mb: 2 }} />
            <Typography color="text.secondary">
              Billing details coming soon. Visit the Billing page to manage payment methods and view history.
            </Typography>
            <Box sx={{ mt: 2 }}>
              <Button component={Link} to="/billing" variant="outlined">Go to Billing</Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  )
}

export default MemberDashboard
