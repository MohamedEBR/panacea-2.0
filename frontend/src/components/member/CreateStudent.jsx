import React, { useState, useEffect } from 'react'
import { Box, Paper, Typography, TextField, Button, Alert, MenuItem, Grid, Card, CardContent, CardActionArea, Chip } from '@mui/material'
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

  useEffect(() => {
    ProgramService.getPrograms().then(setPrograms).catch(()=>{})
  }, [])

  const isEligible = (s, p) => {
    if (!s.name || !s.dob || !s.weight || !s.height || !s.gender) return false
    // Age calc
    const dob = new Date(s.dob)
    const today = new Date()
    let age = today.getFullYear() - dob.getFullYear()
    const m = today.getMonth() - dob.getMonth()
    if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) age--
    if (p.minAge && age < p.minAge) return false
    // Belt: default WHITE on backend; assume rank 0, so only allow if program minBelt is WHITE
    if (p.minBelt && p.minBelt !== 'WHITE') return false
    if (p.genderReq && s.gender && p.genderReq !== s.gender) return false
    // yearsInClub = 0 for new student; allow if minYearsInClub === 0
    if (p.minYearsInClub && p.minYearsInClub > 0) return false
    // capacity check is enforced server-side; optimistic allow here
    return true
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!isAuthenticated()) { navigate('/login'); return }
    setLoading(true)
    setError('')
    try {
      if (!programId) { setError('Please select a program to enroll the student'); return }
      // quick eligibility check in UI
      const selected = programs.find(p => String(p.id) === String(programId))
      if (!isEligible(form, selected || {})) { setError('Student does not meet program requirements'); return }
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
      // Enroll the student into the selected program before creating checkout
      await studentService.addProgram(created.id, Number(programId))
      // Redirect to checkout for current enrollments (backend infers from JWT)
      const checkoutUrl = await PaymentService.createSubscription({})
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
            <Typography variant="subtitle1" sx={{ mb: 1 }}>Select a program</Typography>
            <Grid container spacing={2}>
              {programs.map(p => {
                const disabled = !isEligible(form, p)
                const selected = String(programId) === String(p.id)
                return (
                  <Grid item xs={12} md={6} key={p.id}>
                    <Card variant="outlined" sx={{ opacity: disabled ? 0.5 : 1, borderColor: selected ? '#9d4f4b' : undefined }}>
                      <CardActionArea disabled={disabled} onClick={()=> setProgramId(String(p.id))}>
                        <CardContent>
                          <Typography variant="subtitle1" fontWeight={600}>{p.name}</Typography>
                          <Typography variant="body2" color="text.secondary">${p.rate}/mo</Typography>
                          {disabled && <Chip size="small" label="Not eligible" sx={{ mt: 1 }} />}
                        </CardContent>
                      </CardActionArea>
                    </Card>
                  </Grid>
                )
              })}
            </Grid>
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
