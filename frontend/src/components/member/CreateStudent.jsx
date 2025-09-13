import React, { useState } from 'react'
import { Box, Paper, Typography, TextField, Button, Alert, MenuItem, Grid } from '@mui/material'
import { useAuth } from '../../contexts/AuthContext'
import { useNavigate } from 'react-router-dom'
import studentService from '../../services/studentService'
import ProgramService from '../../lib/programService'
import PaymentService from '../../lib/paymentService'

const genders = ['MALE', 'FEMALE', 'OTHER']
// Belt selection removed; belt defaults to WHITE on backend

const CreateStudent = () => {
  const { user, isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({ name: '', dob: '', weight: '', height: '', medicalConcerns: '', gender: '' })
  const [programs, setPrograms] = useState([])
  const [programId, setProgramId] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm(prev => ({ ...prev, [name]: value }))
  }

  React.useEffect(() => {
    ProgramService.getPrograms().then(setPrograms).catch(()=>{})
  }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!isAuthenticated()) { navigate('/login'); return }
    setLoading(true)
    setError('')
    try {
      if (!programId) {
        setError('Please select a program to enroll the student')
        return
      }
      const payload = {
        name: form.name.trim(),
        dob: form.dob ? new Date(form.dob).toISOString().substring(0,10) : null,
        weight: form.weight ? Number(form.weight) : null,
        height: form.height ? Number(form.height) : null,
        medicalConcerns: form.medicalConcerns || undefined,
        gender: form.gender || undefined,
        belt: 'WHITE',
      }
      // Create student first (no belt selection)
      const created = await studentService.createStudent(user.id, payload)
      // Redirect to checkout for the selected program
      const checkoutUrl = await PaymentService.createSubscription({
        students: [{ studentId: created.id, programIds: [Number(programId)] }]
      })
      window.location.assign(checkoutUrl)
    } catch (e) {
      setError(e.response?.data?.message || 'Failed to create student')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box sx={{ p: { xs: 2, md: 4 } }}>
      <Typography variant="h4" sx={{ mb: 3, fontFamily: '"Saira Semi Condensed"', color: '#9d4f4b' }}>Add Student</Typography>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      <Paper sx={{ p: 2 }} component="form" onSubmit={handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12} md={6}>
            <TextField fullWidth required label="Name" name="name" value={form.name} onChange={handleChange} />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField fullWidth required type="date" label="DOB" name="dob" value={form.dob} onChange={handleChange} InputLabelProps={{ shrink: true }} />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField fullWidth required type="number" label="Weight (lbs)" name="weight" value={form.weight} onChange={handleChange} />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField fullWidth required type="number" label="Height (cm)" name="height" value={form.height} onChange={handleChange} />
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField select fullWidth label="Gender" name="gender" value={form.gender} onChange={handleChange}>
              {genders.map(g => <MenuItem key={g} value={g}>{g}</MenuItem>)}
            </TextField>
          </Grid>
          <Grid item xs={12} md={6}>
            <TextField select fullWidth required label="Program" name="programId" value={programId} onChange={(e)=>setProgramId(e.target.value)}>
              {programs.map(p => <MenuItem key={p.id} value={p.id}>{p.name} — ${p.rate}/mo</MenuItem>)}
            </TextField>
          </Grid>
          <Grid item xs={12}>
            <TextField fullWidth label="Medical Concerns" name="medicalConcerns" value={form.medicalConcerns} onChange={handleChange} multiline rows={3} />
          </Grid>
        </Grid>
        <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
          <Button type="submit" variant="contained" disabled={loading} sx={{ backgroundColor: '#9d4f4b' }}>Create</Button>
          <Button variant="outlined" onClick={() => navigate('/students')}>Cancel</Button>
        </Box>
      </Paper>
    </Box>
  )
}

export default CreateStudent
