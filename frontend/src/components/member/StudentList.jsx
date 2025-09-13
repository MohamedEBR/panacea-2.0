import React, { useEffect, useState } from 'react'
import { Box, Typography, Paper, List, ListItem, ListItemText, ListItemSecondaryAction, IconButton, Button, Divider, Chip, CircularProgress, Alert } from '@mui/material'
import EditIcon from '@mui/icons-material/Edit'
import SchoolIcon from '@mui/icons-material/School'
import { useAuth } from '../../contexts/AuthContext'
import memberService from '../../services/memberService'
import { Link, useNavigate } from 'react-router-dom'

const StatusChip = ({ status }) => (
  <Chip label={status || 'UNKNOWN'} size="small" color={status === 'ACTIVE' ? 'success' : status === 'SUSPENDED' ? 'warning' : status === 'FROZEN' ? 'default' : 'info'} />
)

const StudentList = () => {
  const { user, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [students, setStudents] = useState([])

  useEffect(() => {
    const load = async () => {
      try {
        if (!isAuthenticated()) { navigate('/login'); return }
        const res = await memberService.getMemberStudents(user.id)
        setStudents(res || [])
      } catch (e) {
        setError(e.response?.data?.message || 'Failed to load students')
      } finally {
        setLoading(false)
      }
    }
    if (user?.id) load()
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
        My Students
      </Typography>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper sx={{ p: 2 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h6">Students</Typography>
          <Button component={Link} to="/students/create" variant="contained" sx={{ backgroundColor: '#9d4f4b' }}>Add Student</Button>
        </Box>
        <Divider sx={{ mb: 2 }} />
        {students.length === 0 ? (
          <Typography color="text.secondary">No students yet.</Typography>
        ) : (
          <List>
            {students.map(st => (
              <ListItem key={st.id} divider>
                <ListItemText
                  primary={<>
                    <b>{st.name}</b>{' '}<StatusChip status={st.status} />
                  </>}
                  secondary={st.programs && st.programs.length > 0 ? `Programs: ${st.programs.map(p=>p.name).join(', ')}` : 'No enrolled programs'}
                />
                <ListItemSecondaryAction>
                  <IconButton edge="end" aria-label="programs" title="View Student" onClick={() => navigate(`/students/${st.id}`)}>
                    <SchoolIcon />
                  </IconButton>
                  <IconButton edge="end" aria-label="edit" title="Edit Student" onClick={() => navigate(`/students/${st.id}/edit`)} sx={{ ml: 1 }}>
                    <EditIcon />
                  </IconButton>
                </ListItemSecondaryAction>
              </ListItem>
            ))}
          </List>
        )}
      </Paper>
    </Box>
  )
}

export default StudentList
