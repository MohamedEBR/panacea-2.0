import React, { useEffect, useState } from 'react'
import { Box, Paper, Typography, TextField, Button, Alert, Grid, CircularProgress } from '@mui/material'
import { useNavigate, useParams } from 'react-router-dom'
import studentService from '../../services/studentService'

const EditStudent = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const [form, setForm] = useState({ name: '', dob: '', weight: '', height: '', medicalConcerns: '' })
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    const load = async () => {
      try {
        const s = await studentService.getStudent(id)
        setForm({
          name: s.name || '',
          dob: s.dob ? (s.dob.length > 10 ? s.dob.substring(0,10) : s.dob) : '',
          weight: s.weight ?? '',
          height: s.height ?? '',
          medicalConcerns: s.medicalConcerns || '',
        })
      } catch (e) {
        setError(e.response?.data?.message || 'Failed to load student')
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [id])

  const onChange = (e) => setForm(f => ({ ...f, [e.target.name]: e.target.value }))

  const onSubmit = async (e) => {
    e.preventDefault()
    try {
      setSaving(true)
      const payload = {
        name: form.name.trim(),
        dob: form.dob || null,
        weight: form.weight ? parseInt(form.weight, 10) : null,
        height: form.height ? parseInt(form.height, 10) : null,
        medicalConcerns: form.medicalConcerns || undefined,
      }
      await studentService.updateStudent(id, payload)
      navigate(`/students/${id}`)
    } catch (e) {
      setError(e.response?.data?.message || 'Failed to update student')
    } finally {
      setSaving(false)
    }
  }

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
        Edit Student
      </Typography>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      <Paper sx={{ p: 2 }}>
        <form onSubmit={onSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <TextField fullWidth required label="Name" name="name" value={form.name} onChange={onChange} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth required type="date" label="DOB" name="dob" value={form.dob} onChange={onChange} InputLabelProps={{ shrink: true }} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth required type="number" label="Weight (lbs)" name="weight" value={form.weight} onChange={onChange} />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField fullWidth required type="number" label="Height (cm)" name="height" value={form.height} onChange={onChange} />
            </Grid>
            <Grid item xs={12}>
              <TextField fullWidth label="Medical Concerns" name="medicalConcerns" value={form.medicalConcerns} onChange={onChange} multiline rows={3} />
            </Grid>
          </Grid>
          <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
            <Button type="submit" variant="contained" sx={{ backgroundColor: '#9d4f4b' }} disabled={saving}>
              {saving ? <CircularProgress size={20} sx={{ color: 'white' }} /> : 'Save'}
            </Button>
            <Button variant="outlined" onClick={() => navigate(`/students/${id}`)}>Cancel</Button>
          </Box>
        </form>
      </Paper>
    </Box>
  )
}

export default EditStudent
 
