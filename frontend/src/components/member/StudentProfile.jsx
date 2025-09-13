import React, { useEffect, useState } from 'react'
import { Box, Paper, Typography, Chip, Divider, Button, CircularProgress, Alert } from '@mui/material'
import { useNavigate, useParams } from 'react-router-dom'
import studentService from '../../services/studentService'

const Field = ({ label, value }) => (
  <Box sx={{ display: 'flex', gap: 1, my: 0.5 }}>
    <Typography variant="body2" color="text.secondary" sx={{ minWidth: 140 }}>{label}:</Typography>
    <Typography variant="body2">{value ?? '-'}</Typography>
  </Box>
)

const StudentProfile = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const [student, setStudent] = useState(null)
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const load = async () => {
      try {
        const s = await studentService.getStudent(id)
        setStudent(s)
        const h = await studentService.getStudentHistory(id)
        setHistory(h)
      } catch (e) {
        setError(e.response?.data?.message || 'Failed to load student')
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [id])

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 10 }}>
        <CircularProgress />
      </Box>
    )
  }

  if (!student) {
    return <Alert severity="error">{error || 'Student not found'}</Alert>
  }

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Typography variant="h4" sx={{ mb: 1, fontFamily: '"Saira Semi Condensed"', color: '#9d4f4b' }}>
        {student.name}{' '}
        <Chip size="small" label={student.status} color={student.status === 'ACTIVE' ? 'success' : 'default'} />
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>Student ID: {student.id}</Typography>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <Paper sx={{ p: 2, mb: 2 }}>
        <Typography variant="h6" sx={{ mb: 1 }}>Profile</Typography>
        <Field label="DOB" value={student.dob} />
        <Field label="Weight" value={student.weight} />
        <Field label="Height" value={student.height} />
        <Field label="Gender" value={student.gender} />
        <Field label="Belt" value={student.belt} />
        <Field label="Registered At" value={student.registeredAt} />
        <Field label="Years In Club" value={student.yearsInClub} />
        <Field label="Medical Concerns" value={student.medicalConcerns} />

        <Divider sx={{ my: 2 }} />
        <Typography variant="subtitle2" sx={{ mb: 1 }}>Programs</Typography>
        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
          {(student.programs || []).map(p => (
            <Chip key={p.id} label={p.name} variant="outlined" />
          ))}
          {(student.programs || []).length === 0 && (
            <Typography color="text.secondary">No enrolled programs</Typography>
          )}
        </Box>
      </Paper>

      <Paper sx={{ p: 2 }}>
        <Typography variant="h6" sx={{ mb: 1 }}>History</Typography>
        {history.length === 0 ? (
          <Typography color="text.secondary">No history yet.</Typography>
        ) : (
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
            {history.map((h) => (
              <Box key={h.id} sx={{ display: 'flex', gap: 2 }}>
                <Typography variant="body2" sx={{ minWidth: 160 }}>
                  {new Date(h.timestamp).toLocaleString()}
                </Typography>
                <Typography variant="body2">{h.action} - {h.program?.name}</Typography>
              </Box>
            ))}
          </Box>
        )}
      </Paper>

      <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
        <Button variant="outlined" onClick={() => navigate('/students')}>Back</Button>
        <Button variant="contained" sx={{ backgroundColor: '#9d4f4b' }} onClick={() => navigate(`/students/${id}/edit`)}>Edit</Button>
      </Box>
    </Box>
  )
}

export default StudentProfile
