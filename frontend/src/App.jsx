import { Routes, Route } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import Header from "./components/shared/Header";
import Footer from "./components/shared/Footer";
import "./App.css";
import { Home, About, Contact, Programs, Error, Login, Signup, Blogs, CreateBlog, EditBlog, BlogId, PaymentSuccess, PaymentCancel, AdminSecurity } from "./pages";
import Box from "@mui/material/Box";
import MemberDashboard from './components/member/MemberDashboard';
import MemberProfile from './components/member/MemberProfile';
import StudentList from './components/member/StudentList';
import CreateStudent from './components/member/CreateStudent';
import EditStudent from './components/member/EditStudent';
import StudentProfile from './components/member/StudentProfile';

function App() {
  return (
    <AuthProvider>
      <Box
        sx={{
          overflow: "hidden",
        }}
      >
        <Header />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/programs" element={<Programs />} />
          <Route path="/blogs/" element={<Blogs />} />
          <Route path="/blogs/:id" element={<BlogId />} />
          <Route 
            path="/blogs/new" 
            element={
              <ProtectedRoute adminOnly={true}>
                <CreateBlog />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/blogs/:id/edit" 
            element={
              <ProtectedRoute adminOnly={true}>
                <EditBlog />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/admin/security" 
            element={
              <ProtectedRoute adminOnly={true}>
                <AdminSecurity />
              </ProtectedRoute>
            } 
          />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/signup/success" element={<PaymentSuccess />} />
          <Route path="/signup/cancel" element={<PaymentCancel />} />

          {/* Phase 1: Member routes */}
          <Route 
            path="/dashboard" 
            element={
              <ProtectedRoute>
                <MemberDashboard />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/profile" 
            element={
              <ProtectedRoute>
                <MemberProfile />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/students" 
            element={
              <ProtectedRoute>
                <StudentList />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/students/create" 
            element={
              <ProtectedRoute>
                <CreateStudent />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/students/:id" 
            element={
              <ProtectedRoute>
                <StudentProfile />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/students/:id/edit" 
            element={
              <ProtectedRoute>
                <EditStudent />
              </ProtectedRoute>
            } 
          />

          <Route path="*" element={<Error />} />
        </Routes>
        <Footer />
      </Box>
    </AuthProvider>
  );
}

export default App;
