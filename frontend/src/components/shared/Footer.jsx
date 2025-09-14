import {
  MDBFooter,
  MDBContainer,
  MDBRow,
  MDBCol,
  MDBIcon,
  MDBBtn,
} from "mdb-react-ui-kit";
import {Link} from 'react-router-dom'
export default function Footer() {
  return (
    <MDBFooter
      bgColor="none"
      style={{
        backgroundColor: "#9d4f4b",
        color: "#fff",
      }}
      className="text-center text-lg-start "
    >
      <section className="d-flex justify-content-center justify-content-lg-between p-4 border-bottom">
        <div className="me-5 d-none d-lg-block">
          <span>Get connected with us on social networks:</span>
        </div>

        <div>
          <MDBBtn
            floating
            className="m-1 me-4"
            rippleColor="light"
            color="light"
            size="lg"
            href="https://www.facebook.com/people/Panacea-International-Karate-Academy/100069371208804/"
            role="button"
            target="_blank"
            rel="noopener noreferrer"
          >
            <MDBIcon fab icon="facebook-f" />
          </MDBBtn>
          <MDBBtn
            floating
            className="m-1 me-4"
            rippleColor="light"
            color="light"
            size="lg"
            href="https://www.google.com/search?q=pancea+karate+acdemy+google"
            role="button"
            target="_blank"
            rel="noopener noreferrer"
          >
            <MDBIcon fab icon="google" />
          </MDBBtn>
          <MDBBtn
            floating
            className="m-1 me-4"
            rippleColor="light"
            color="light"
            size="lg"
            href="#!"
            role="button"
          >
            <MDBIcon fab icon="instagram" />
          </MDBBtn>
        </div>
      </section>

      <section className="">
        <MDBContainer className="text-center text-md-start mt-5">
          <MDBRow className="mt-3">
            <MDBCol md="3" lg="4" xl="3" className="mx-auto mb-4">
              <h6 className="text-uppercase fw-bold mb-4">
                <MDBIcon color="light" icon="gem" className="me-3"  />
                Panacea Karate Academy
              </h6>
              <h6 className="text-uppercase fw-bold mb-4">Contact</h6>
              <p>
                <MDBIcon color="light" icon="home" className="me-2" />
                1201 Britannia Road West, Mississauga, ON
              </p>
              <p>
                <MDBIcon color="light" icon="envelope" className="me-3" />
                info@panaceakarateacademy.com
              </p>
              <p>
                <MDBIcon color="light" icon="phone" className="me-3" /> + 01
                647 772 2249
              </p>
            </MDBCol>

            <MDBCol md="2" lg="2" xl="2" className="mx-auto mb-4">
              <h6 className="text-uppercase fw-bold mb-4">Links</h6>
              <p>
                <Link to='/about' style={{color: '#fff', textDecoration: 'none'}}>
                  About
                </Link>
              </p>
              <p>
                <Link to='/programs' style={{color: '#fff', textDecoration: 'none'}}>
                  Programs
                </Link>
              </p>
              <p>
                <Link to='/blogs' style={{color: '#fff', textDecoration: 'none'}}>
                  Blogs
                </Link>
              </p>
              <p>
                <Link to='/contact' style={{color: '#fff', textDecoration: 'none'}}>
                  Contact
                </Link>
              </p>
            </MDBCol>

            <MDBCol md="3" lg="2" xl="2" className="mx-auto mb-4 px-0">
              <h6 className="text-uppercase fw-bold mb-4">Working Hours</h6>
              <p>
                  6pm to 7:15pm Monday
              </p>
              <p>
                  4:30pm to 7:15pm Tuesday
              </p>
              <p>
                  6pm to 7:15pm Wednesday
              </p>
              <p>
                  4:30pm to 7:15pm Thursday
              </p>
              <p>
                  9am to 12pm Saturday
              </p>
              <p>
                  4:30pm to 7:15pm Sunday
              </p>
            </MDBCol>

            <MDBCol
              md="4"
              lg="3"
              xl="3"
              className="mx-auto mb-md-0 mb-4"
            ></MDBCol>
          </MDBRow>
        </MDBContainer>
      </section>

      <div
        className="text-center p-4"
        style={{ backgroundColor: "rgba(0, 0, 0, 0.05)" }}
      >
        © 2024 Copyright:
        <a className="text-reset fw-bold" href="https://panaceakarateacademy.ca/" target="_blank" rel="noopener noreferrer">
          panaceakarateacademy.ca
        </a>
      </div>
    </MDBFooter>
  );
}
