import React, { useEffect, useState } from 'react'
import { Box, Typography, Paper, TextField, Grid, Button, Divider, Alert, CircularProgress } from '@mui/material'
import { useAuth } from '../../contexts/AuthContext'
import memberService from '../../services/memberService'

const MemberProfile = () => {
  const { user } = useAuth()
  const memberId = user?.id
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const [profile, setProfile] = useState({
    name: '',
    lastName: '',
    email: '',
    phone: '',
    address: '',
    city: '',
    postalCode: '',
  })

  const [passwords, setPasswords] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  })

  useEffect(() => {
    const load = async () => {
      try {
        const m = await memberService.getMemberProfile(memberId)
        setProfile({
          name: m.name || '',
          lastName: m.lastName || '',
          email: m.email || '',
          phone: m.phone || '',
          address: m.address || '',
          city: m.city || '',
          postalCode: m.postalCode || '',
        })
      } catch (e) {
        setError(e.response?.data?.message || 'Failed to load profile')
      } finally {
        setLoading(false)
      }
    }
    if (memberId) load()
  }, [memberId])

  const onChange = (e) => {
    const { name, value } = e.target
    setProfile(p => ({ ...p, [name]: value }))
  }

  const saveProfile = async () => {
    setSaving(true)
    setError('')
    setSuccess('')
    try {
      await memberService.updateMemberProfile(memberId, profile)
      setSuccess('Profile updated successfully')
    } catch (e) {
      setError(e.response?.data?.message || 'Failed to update profile')
    } finally {
      setSaving(false)
    }
  }

  const changePassword = async () => {
    setSaving(true)
    setError('')
    setSuccess('')

    if (!passwords.newPassword || passwords.newPassword !== passwords.confirmPassword) {
      setError('New password and confirmation do not match')
      setSaving(false)
      return
    }
    try {
      await memberService.updatePassword(memberId, passwords)
      setSuccess('Password updated successfully')
      setPasswords({ oldPassword: '', newPassword: '', confirmPassword: '' })
    } catch (e) {
      setError(e.response?.data?.message || 'Failed to update password')
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
        My Profile
      </Typography>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

      <Grid container spacing={3}>
        <Grid item xs={12} md={7}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6">Profile Information</Typography>
            <Divider sx={{ my: 2 }} />
            <Grid container spacing={2}>
              <Grid item xs={12} md={6}>
                <TextField name="name" label="First Name" fullWidth value={profile.name} onChange={onChange} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField name="lastName" label="Last Name" fullWidth value={profile.lastName} onChange={onChange} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField name="email" label="Email" fullWidth value={profile.email} disabled />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField name="phone" label="Phone" fullWidth value={profile.phone} onChange={onChange} />
              </Grid>
              <Grid item xs={12}>
                <TextField name="address" label="Address" fullWidth value={profile.address} onChange={onChange} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField name="city" label="City" fullWidth value={profile.city} onChange={onChange} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField name="postalCode" label="Postal Code" fullWidth value={profile.postalCode} onChange={onChange} />
              </Grid>
            </Grid>
            <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
              <Button onClick={saveProfile} variant="contained" disabled={saving} sx={{ backgroundColor: '#9d4f4b' }}>Save Changes</Button>
            </Box>
          </Paper>
        </Grid>

        <Grid item xs={12} md={5}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6">Change Password</Typography>
            <Divider sx={{ my: 2 }} />
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField type="password" label="Current Password" fullWidth value={passwords.oldPassword} onChange={(e)=>setPasswords(p=>({...p, oldPassword:e.target.value}))} />
              </Grid>
              <Grid item xs={12}>
                <TextField type="password" label="New Password" fullWidth value={passwords.newPassword} onChange={(e)=>setPasswords(p=>({...p, newPassword:e.target.value}))} />
              </Grid>
              <Grid item xs={12}>
                <TextField type="password" label="Confirm New Password" fullWidth value={passwords.confirmPassword} onChange={(e)=>setPasswords(p=>({...p, confirmPassword:e.target.value}))} />
              </Grid>
            </Grid>
            <Box sx={{ mt: 2, display: 'flex', gap: 1 }}>
              <Button onClick={changePassword} variant="outlined" disabled={saving}>Update Password</Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  )
}

export default MemberProfile
