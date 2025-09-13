import React, { useEffect, useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import ProgramService from "../lib/programService";
import PaymentService from "../lib/paymentService";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  Box,
  Grid,
  TextField,
  Button,
  Typography,
  Card,
  CardContent,
  CardMedia,
  CardActionArea,
  Checkbox,
  FormControlLabel,
  Divider,
  Chip,
  Stack,
  MenuItem
} from "@mui/material";
import images from "../constants/images";
import { motion } from "framer-motion";

const emptyMember = {
  name: "",
  lastName: "",
  email: "",
  password: "",
  birthDate: "",
  phone: "",
  address: "",
  unit: "",
  city: "",
  postalCode: ""
};

const emptyStudent = {
  name: "",
  dob: "",
  weight: "",
  height: "",
  medicalConcerns: "",
  gender: "",
  belt: "",
  programIds: []
};

const Signup = () => {
  const navigate = useNavigate();
  const { register, loading } = useAuth();
  const [step, setStep] = useState(1); // 1 = member, 2 = students, 3 = review & pay
  const [member, setMember] = useState({ ...emptyMember });
  const [students, setStudents] = useState([{ ...emptyStudent }]);
  const [programs, setPrograms] = useState([]);
  const [passwordError, setPasswordError] = useState("");

  useEffect(() => {
    // Fetch programs for selection
    ProgramService.getPrograms()
      .then(setPrograms)
      .catch(() => toast.error("Failed to load programs"));
  }, []);

  const genders = ["MALE","FEMALE","OTHER"]; 

  const passwordRegex = useMemo(
    () => /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
    []
  );

  const handleMemberChange = (e) => {
    const { name, value } = e.target;
    setMember(prev => ({ ...prev, [name]: value }));
    if (name === "password") {
      if (!passwordRegex.test(value)) {
        setPasswordError("Min 8 chars, 1 uppercase, 1 number, 1 special char");
      } else {
        setPasswordError("");
      }
    }
  };

  const handleStudentChange = (idx, e) => {
    const { name, value } = e.target;
    setStudents(prev => prev.map((s,i) => i===idx ? { ...s, [name]: value } : s));
  };

  const handleProgramToggle = (idx, programId) => {
    // Limit to only one program selection
    setStudents(prev => prev.map((s,i) => i===idx ? { ...s, programIds: [programId] } : s));
  };

  const addStudent = () => setStudents(prev => [...prev, { ...emptyStudent }]);
  const removeStudent = (idx) => setStudents(prev => prev.filter((_,i)=>i!==idx));

  const nextStep = () => setStep((s) => Math.min(3, s + 1));
  const prevStep = () => setStep((s) => Math.max(1, s - 1));

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Step progression
    if (step === 1) return nextStep();
    if (step === 2) return nextStep();

    // Transform data to backend contract
    const payload = {
      ...member,
      birthDate: member.birthDate, // yyyy-MM-dd
      students: students.map(s => ({
        name: s.name,
        dob: s.dob,
        weight: s.weight ? Number(s.weight) : null,
        height: s.height ? Number(s.height) : null,
        medicalConcerns: s.medicalConcerns || null,
        gender: s.gender,
        // Do not let member pick belt; default to WHITE on creation
        belt: "WHITE",
        programIds: s.programIds
      }))
    };

    // Simple client-side checks matching backend annotations
    if (!payload.students.length || payload.students.some(s => !s.name || !s.dob || !s.weight || !s.height || !s.gender || !s.programIds.length || s.programIds.length !== 1)) {
      toast.error("Please complete all required student fields");
      return;
    }

    try {
      const res = await register(payload);
      if (res.success) {
        toast.success("Registration successful! Redirecting to secure checkout...");
        // Construct subscription payload using created member/student IDs if available later.
        // For now, use a simplified approach: backend can resolve current member from JWT and students by name.
        const subscriptionPayload = {
          memberId: null, // backend should infer from JWT
          students: students.map((s) => ({
            studentId: null, // optional for MVP
            programIds: s.programIds,
          })),
        };
        const checkoutUrl = await PaymentService.createSubscription(subscriptionPayload);
        window.location.assign(checkoutUrl);
      } else {
        toast.error(res.message || "Registration failed");
      }
    } catch (err) {
      console.error(err);
      toast.error("Registration or checkout failed");
    }
  };

  const canGoNext =
    member.name && member.lastName && member.email && member.password && member.birthDate &&
    member.phone && member.address && member.city && member.postalCode && !passwordError;

  const isStudentComplete = (s) => !!(s.name && s.dob && s.weight && s.height && s.gender);

  const isEligible = (s, p) => {
    // Grey-out logic after student info is complete
    if (!isStudentComplete(s)) return false;
    // Age check
    const age = (() => {
      const dob = new Date(s.dob);
      const today = new Date();
      let a = today.getFullYear() - dob.getFullYear();
      const m = today.getMonth() - dob.getMonth();
      if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) a--;
      return a;
    })();
    if (age < (p.minAge || 0)) return false;
    // Belt is WHITE at registration
    const beltRank = 0; // WHITE
    const minBeltRank = (() => {
      const map = { WHITE:0, YELLOW:1, ORANGE:2, GREEN:3, BLUE:4, PURPLE_II:5, PURPLE_I:6, BROWN_III:7, BROWN_II:8, BROWN_I:9, BLACK:10 };
      return map[p.minBelt || 'WHITE'] ?? 0;
    })();
    if (beltRank < minBeltRank) return false;
    // Years in club is 0 at registration
    if ((p.minYearsInClub || 0) > 0) return false;
    // Gender requirement if present
    if (p.genderReq && p.genderReq !== s.gender) return false;
    // Capacity
    if (p.enrolledCount >= p.capacity) return false;
    return true;
  };

  const programImage = (p) => {
    const name = (p.name || "").toLowerCase();
    if (name.includes("beginner")) return images.programBeginner;
    if (name.includes("intermediate")) return images.programIntermediate;
    if (name.includes("elite") || name.includes("advanced")) return images.programElite;
    if (name.includes("child") || name.includes("kids")) return images.programBeginnerChild;
    return images.programBeginner;
  };

  return (
    <Box sx={{ maxWidth: 1000, mx: "auto", px: { xs: 2, md: 4 }, py: 4 }}>
      <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4 }}>
        <Typography variant="h4" fontWeight={700} gutterBottom color="primary">
          Create your account
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
          Step {step} of 3
        </Typography>
      </motion.div>
      <Box component="form" onSubmit={handleSubmit}>
        {step === 1 && (
          <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.3 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>Member information</Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} md={6}>
                <TextField label="First Name" name="name" value={member.name} onChange={handleMemberChange} fullWidth required disabled={loading} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField label="Last Name" name="lastName" value={member.lastName} onChange={handleMemberChange} fullWidth required disabled={loading} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField label="Email" type="email" name="email" value={member.email} onChange={handleMemberChange} fullWidth required disabled={loading} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  label="Password"
                  type="password"
                  name="password"
                  value={member.password}
                  onChange={handleMemberChange}
                  fullWidth
                  required
                  error={!!passwordError}
                  helperText={passwordError || ""}
                  disabled={loading}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField
                  label="Birth date"
                  type="date"
                  name="birthDate"
                  value={member.birthDate}
                  onChange={handleMemberChange}
                  fullWidth
                  required
                  InputLabelProps={{ shrink: true }}
                  disabled={loading}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField label="Phone" name="phone" value={member.phone} onChange={handleMemberChange} fullWidth required disabled={loading} />
              </Grid>
              <Grid item xs={12}>
                <TextField label="Address" name="address" value={member.address} onChange={handleMemberChange} fullWidth required disabled={loading} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField label="Apt/Suite (optional)" name="unit" value={member.unit} onChange={handleMemberChange} fullWidth disabled={loading} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField label="City" name="city" value={member.city} onChange={handleMemberChange} fullWidth required disabled={loading} />
              </Grid>
              <Grid item xs={12} md={6}>
                <TextField label="Postal Code" name="postalCode" value={member.postalCode} onChange={handleMemberChange} fullWidth required disabled={loading} />
              </Grid>
            </Grid>
            <Stack direction="row" spacing={2} sx={{ mt: 3 }}>
              <Button variant="contained" onClick={nextStep} disabled={!canGoNext || loading}>
                Next: Add students
              </Button>
              <Button variant="text" component={Link} to="/login">Already have an account? Login</Button>
            </Stack>
          </motion.div>
        )}

  {step === 2 && (
          <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.3 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>Students</Typography>
            <Stack spacing={2}>
              {students.map((s, idx) => (
                <motion.div key={idx} initial={{ opacity: 0, y: 6 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.25 }}>
                <Card variant="outlined" sx={{ borderRadius: 2 }}>
                  <CardContent>
                    <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 2 }}>
                      <Typography variant="subtitle1" fontWeight={600}>Student {idx+1}</Typography>
                      {students.length > 1 && (
                        <Button color="error" size="small" onClick={() => removeStudent(idx)} disabled={loading}>Remove</Button>
                      )}
                    </Stack>
                    <Grid container spacing={2}>
                      <Grid item xs={12} md={6}>
                        <TextField label="Full Name" name="name" value={s.name} onChange={(e)=>handleStudentChange(idx,e)} fullWidth required disabled={loading} />
                      </Grid>
                      <Grid item xs={12} md={6}>
                        <TextField label="Date of Birth" type="date" name="dob" value={s.dob} onChange={(e)=>handleStudentChange(idx,e)} fullWidth required InputLabelProps={{ shrink: true }} disabled={loading} />
                      </Grid>
                      <Grid item xs={12} md={6}>
                        <TextField label="Weight (kg)" type="number" name="weight" value={s.weight} onChange={(e)=>handleStudentChange(idx,e)} fullWidth required disabled={loading} />
                      </Grid>
                      <Grid item xs={12} md={6}>
                        <TextField label="Height (cm)" type="number" name="height" value={s.height} onChange={(e)=>handleStudentChange(idx,e)} fullWidth required disabled={loading} />
                      </Grid>
                      <Grid item xs={12}>
                        <TextField label="Medical Concerns" name="medicalConcerns" value={s.medicalConcerns} onChange={(e)=>handleStudentChange(idx,e)} fullWidth disabled={loading} />
                      </Grid>
                      <Grid item xs={12} md={6}>
                        <TextField select label="Gender" name="gender" value={s.gender} onChange={(e)=>handleStudentChange(idx,e)} fullWidth required disabled={loading}>
                          <MenuItem value=""><em>None</em></MenuItem>
                          {genders.map(g => (
                            <MenuItem key={g} value={g}>{g}</MenuItem>
                          ))}
                        </TextField>
                      </Grid>
                    </Grid>

                    <Divider sx={{ my: 2 }}><Chip label="Select Programs" /></Divider>
                    <Grid container spacing={2}>
                      {programs.map(p => {
                        const selected = s.programIds.includes(p.id);
                        const eligible = isEligible(s, p);
                        return (
                          <Grid item xs={12} sm={6} md={4} key={p.id}>
                            <Card variant={selected ? "elevation" : "outlined"} sx={{ borderColor: selected ? "primary.main" : undefined, borderRadius: 2, overflow: 'hidden', opacity: eligible ? 1 : 0.5 }}>
                              <CardActionArea disabled={!eligible} onClick={() => handleProgramToggle(idx, p.id)}>
                                <CardMedia component="img" height="120" image={programImage(p)} alt={p.name} />
                                <CardContent>
                                  <Stack direction="row" justifyContent="space-between" alignItems="center">
                                    <Box>
                                      <Typography variant="subtitle1" fontWeight={600}>{p.name}</Typography>
                                      <Typography variant="body2" color="text.secondary">{p.description}</Typography>
                                      <Typography variant="caption" color="text.secondary">${p.rate} / month</Typography>
                                      {!eligible && isStudentComplete(s) && (
                                        <Typography variant="caption" color="error" display="block">Not eligible</Typography>
                                      )}
                                    </Box>
                                    <Checkbox checked={selected} onChange={() => handleProgramToggle(idx, p.id)} onClick={(e) => e.stopPropagation()} disabled={!eligible} />
                                  </Stack>
                                </CardContent>
                              </CardActionArea>
                            </Card>
                          </Grid>
                        );
                      })}
                    </Grid>
                  </CardContent>
                </Card>
                </motion.div>
              ))}
            </Stack>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mt: 2 }}>
              <Button variant="outlined" onClick={addStudent} disabled={loading}>+ Add another student</Button>
              <Box sx={{ flexGrow: 1 }} />
              <Button variant="text" onClick={prevStep} disabled={loading}>Back</Button>
              <Button type="submit" variant="contained" disabled={loading}>{loading ? 'Submitting...' : 'Review & Pay'}</Button>
            </Stack>
          </motion.div>
        )}

        {step === 3 && (
          <motion.div initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.3 }}>
            <Typography variant="h6" sx={{ mb: 2 }}>Review & Pay</Typography>
            <Card variant="outlined" sx={{ borderRadius: 2 }}>
              <CardContent>
                <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 1 }}>Member</Typography>
                <Typography variant="body2" color="text.secondary">{member.name} {member.lastName} · {member.email}</Typography>
                <Typography variant="body2" color="text.secondary">{member.address}{member.unit ? `, ${member.unit}` : ''}, {member.city} {member.postalCode}</Typography>
                <Divider sx={{ my: 2 }} />
                <Typography variant="subtitle1" fontWeight={600} sx={{ mb: 1 }}>Students & Programs</Typography>
                <Stack spacing={1}>
                  {students.map((s, idx) => {
                    const prog = programs.find(p => s.programIds[0] === p.id);
                    return (
                      <Box key={idx} sx={{ display: 'flex', justifyContent: 'space-between' }}>
                        <Typography variant="body2">{s.name} · {prog ? prog.name : 'No program'}</Typography>
                        <Typography variant="body2">{prog ? `$${prog.rate}/mo` : '-'}</Typography>
                      </Box>
                    );
                  })}
                </Stack>
                <Divider sx={{ my: 2 }} />
                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                  <Typography variant="subtitle1">Total due today</Typography>
                  <Typography variant="subtitle1">
                    {(() => {
                      const total = students.reduce((sum, s) => {
                        const prog = programs.find(p => s.programIds[0] === p.id);
                        return sum + (prog ? Number(prog.rate) : 0);
                      }, 0);
                      return `$${total.toFixed(2)}`;
                    })()}
                  </Typography>
                </Box>
                <Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 1 }}>
                  Use Stripe test card 4242 4242 4242 4242, any future expiry, any CVC, any ZIP.
                </Typography>
              </CardContent>
            </Card>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mt: 2 }}>
              <Button variant="text" onClick={prevStep} disabled={loading}>Back</Button>
              <Button type="submit" variant="contained" disabled={loading}>{loading ? 'Processing…' : 'Go to secure checkout'}</Button>
            </Stack>
          </motion.div>
        )}
      </Box>
      <ToastContainer />
    </Box>
  );
};

export default Signup;