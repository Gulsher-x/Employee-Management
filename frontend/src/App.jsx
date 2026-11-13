
import { useState, useEffect } from "react";
import "./App.css";
import Login from "./Login";
import { apiRequest } from "./api";

function App() {
  const [role, setRole] = useState(
      sessionStorage.getItem("userRole")
  );

  const [activePage, setActivePage] = useState("Employees");

  const [employees, setEmployees] = useState([]);
  const [profile, setProfile] = useState(null);
  const [userProfile, setUserProfile] = useState(null);
  const [isEditingProfile, setIsEditingProfile] = useState(false);

  const [editProfile, setEditProfile] = useState({
    name: "",
    email: "",
    phone: ""
  });

  const [stats, setStats] = useState({
    totalUsers: 0,
    totalEmployees: 0,
    totalDepartments: 0,
  });

  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [search, setSearch] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [editId, setEditId] = useState(null);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    department: "IT",
  });

  // Login
  function handleLoginSuccess(userRole) {
    sessionStorage.setItem("userRole", userRole);
    setRole(userRole);
  }

  // Logout
  function handleLogout() {
    sessionStorage.removeItem("authToken");
    sessionStorage.removeItem("userRole");
    setRole(null);
  }

  // Load data
  useEffect(() => {
    if (role === "ADMIN") {
      loadEmployees();
      loadDashboard();
      loadAdminProfile();
    } else if (role === "USER") {
      loadUserProfile();
    }
  }, [role]);

  async function loadEmployees() {
    try {
      const data = await apiRequest("/admin/employees");
      setEmployees(data);
    } catch (error) {
      console.error("Employees fetch error:", error);
    }
  }

  async function loadDashboard() {
    try {
      const data = await apiRequest("/admin/dashboard");
      setStats(data);
    } catch (error) {
      console.error("Dashboard stats error:", error);
    }
  }
  async function loadAdminProfile() {
    try {
      const data = await apiRequest("/admin/profile");
      setProfile(data);
    } catch (error) {
      console.error("Profile fetch error:", error);
    }
  }
  async function loadUserProfile() {
    try {
      const data = await apiRequest("/user");
      setUserProfile(data);
    } catch (error) {
      console.error("User profile fetch error:", error);
    }
  }
  // Change Admin Password
  async function handleChangePassword(e) {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      alert("New password and confirm password do not match!");
      return;
    }

    if (newPassword.length < 8) {
      alert("Password must be at least 8 characters!");
      return;
    }

    try {
      await apiRequest("/user/change-password", {
        method: "PUT",
        body: JSON.stringify({
          currentPassword,
          newPassword,
        }),
      });

      // Update Basic Auth token with new password
      const username = atob(sessionStorage.getItem("authToken")).split(":")[0];

      const newToken = btoa(`${username}:${newPassword}`);
      sessionStorage.setItem("authToken", newToken);

      alert("Password changed successfully!");

      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");

    } catch (error) {
      console.error("Password change error:", error);
      alert("Password change failed! Check your current password.");
    }
  }
  const handleUpdateProfile = async () => {
    if (
        !editProfile.name.trim() ||
        !editProfile.email.trim() ||
        !editProfile.phone.trim()
    ) {
      alert("Please fill all fields!");
      return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailPattern.test(editProfile.email)) {
      alert("Please enter a valid email!");
      return;
    }

    if (!/^\d{10}$/.test(editProfile.phone)) {
      alert("Phone number must be 10 digits!");
      return;
    }

    try {
      const updatedProfile = await apiRequest("/user/profile", {
        method: "PUT",
        body: JSON.stringify(editProfile)
      });

      setUserProfile(updatedProfile);
      setIsEditingProfile(false);

      alert("Profile updated successfully!");
    } catch (error) {
      alert("Unable to update profile!");
      console.error(error);
    }
  };
  // Form input change
  function handleChange(e) {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  }

  // Add / Update Employee
  async function handleSubmit(e) {
    e.preventDefault();

    try {
      if (editId !== null) {
        await apiRequest(`/admin/employees/${editId}`, {
          method: "PUT",
          body: JSON.stringify(formData),
        });
      } else {
        await apiRequest("/admin/employees", {
          method: "POST",
          body: JSON.stringify(formData),
        });
      }

      await loadEmployees();
      await loadDashboard();

      setFormData({
        name: "",
        email: "",
        phone: "",
        department: "IT",
      });

      setEditId(null);
      setShowForm(false);

      alert("Employee saved successfully!");

    } catch (error) {
      console.error("Save employee error:", error);
      alert("Employee save failed!");
    }
  }

  // Edit Employee
  function handleEdit(employee) {
    setEditId(employee.id);

    setFormData({
      name: employee.name || "",
      email: employee.email || "",
      phone: employee.phone || "",
      department: employee.department || "IT",
    });

    setShowForm(true);
  }

  // Delete Employee
  async function handleDelete(id) {
    const confirmDelete = window.confirm(
        "Are you sure you want to delete this employee?"
    );

    if (!confirmDelete) return;

    try {
      await apiRequest(`/admin/employees/${id}`, {
        method: "DELETE",
      });

      await loadEmployees();
      await loadDashboard();

      alert("Employee deleted successfully!");

    } catch (error) {
      console.error("Delete employee error:", error);
      alert("Employee delete failed!");
    }
  }
  // Department-wise employee count
  const departmentCounts = employees.reduce((acc, employee) => {
    const department = employee.department || "Other";
    acc[department] = (acc[department] || 0) + 1;
    return acc;
  }, {});
  // Search Employee
  const filteredEmployees = employees.filter((employee) =>
      employee.name.toLowerCase().includes(search.toLowerCase())
  );

  // Login page
  if (!role) {
    return <Login onLogin={handleLoginSuccess} />;
  }

  return (
      <div className="app">

        {/* Sidebar */}
        <aside className="sidebar">

          <h2>Synapse EMS</h2>

          <nav>

            {role === "ADMIN" && (
                <>
                  <p
                      className={activePage === "Dashboard" ? "active" : ""}
                      onClick={() => {
                        setActivePage("Dashboard");
                        setShowForm(false);
                      }}
                  >
                    Dashboard
                  </p>

                  <p
                      className={activePage === "Employees" ? "active" : ""}
                      onClick={() => {
                        setActivePage("Employees");
                        setShowForm(false);
                      }}
                  >
                    Employees
                  </p>

                  <p
                      onClick={() => {
                        setActivePage("Employees");
                        setShowForm(true);
                        setEditId(null);
                        setFormData({
                          name: "",
                          email: "",
                          phone: "",
                          department: "IT",
                        });
                      }}
                  >
                    Add Employee
                  </p>

                  <p
                      className={activePage === "My Profile" ? "active" : ""}
                      onClick={() => {
                        setActivePage("My Profile");
                        setShowForm(false);
                      }}
                  >
                    My Profile
                  </p>
                </>
            )}

            {role === "USER" && (
                <p
                    className={activePage === "Employees" ? "active" : ""}
                    onClick={() => setActivePage("Employees")}
                >
                  My Dashboard
                </p>
            )}

          </nav>

          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>

        </aside>

        {/* Main Content */}
        <main className="main">
          {/* User Dashboard */}
          {role === "USER" && activePage === "Employees" && (
              <section className="employee-section">
                <h2>Welcome, {userProfile?.name || "Employee"}!</h2>
                <p>Welcome to your Employee Dashboard.</p>

                {userProfile ? (
                    <div className="profile-card">
                      <div className="profile-avatar">
                        {userProfile.name?.charAt(0).toUpperCase()}
                      </div>

                      <h2>{userProfile.name}</h2>
                      <span className="role-badge">EMPLOYEE</span>

                      <div className="profile-details">
                        <div>
                          <strong>Email</strong>
                          <p>{userProfile.email}</p>
                        </div>

                        <div>
                          <strong>Phone</strong>
                          <p>{userProfile.phone}</p>
                        </div>

                        <div>
                          <strong>Department</strong>
                          <p>{userProfile.department}</p>
                        </div>

                        <div>
                          <strong>Role / Designation</strong>
                          <p>{userProfile.role}</p>
                        </div>
                      </div>
                    </div>
                ) : (
                    <p>Loading your profile...</p>
                )}
                <button
                    disabled={!userProfile}
                    onClick={() => {
                      setEditProfile({
                        name: userProfile.name || "",
                        email: userProfile.email || "",
                        phone: userProfile.phone || ""
                      });
                      setIsEditingProfile(true);
                    }}
                >
                  Edit Profile
                </button>

                <button onClick={() => setActivePage("Change Password")}>
                  Change Password
                </button>
                {isEditingProfile && (
                    <div className="profile-form">
                      <h3>Edit Profile</h3>

                      <input
                          type="text"
                          placeholder="Enter Name"
                          value={editProfile.name}
                          onChange={(e) =>
                              setEditProfile({ ...editProfile, name: e.target.value })
                          }
                      />

                      <input
                          type="email"
                          placeholder="Enter Email"
                          value={editProfile.email}
                          onChange={(e) =>
                              setEditProfile({ ...editProfile, email: e.target.value })
                          }
                      />

                      <input
                          type="tel"
                          placeholder="Enter Phone"
                          value={editProfile.phone}
                          maxLength={10}
                          inputMode="numeric"
                          required
                          onChange={(e) =>
                              setEditProfile({
                                ...editProfile,
                                phone: e.target.value.replace(/\D/g, "")
                              })
                          }
                      />

                      <button onClick={handleUpdateProfile}>
                        Save Changes
                      </button>

                      <button
                          onClick={() => {
                            setIsEditingProfile(false);
                            setEditProfile({
                              name: userProfile?.name || "",
                              email: userProfile?.email || "",
                              phone: userProfile?.phone || ""
                            });
                          }}
                      >
                        Cancel
                      </button>
                    </div>
                )}
              </section>
          )}

          {/* Header */}
          <header>
            <div>
              <h1>
                {role === "USER"
                    ? activePage === "Change Password"
                        ? "Change Password"
                        : "My Dashboard"
                    : activePage}
              </h1>

              <p>
                {role === "USER"
                    ? activePage === "Change Password"
                        ? "Update your account password."
                        : "Welcome to your employee dashboard."
                    : activePage === "Dashboard"
                        ? "Welcome to your admin dashboard."
                        : activePage === "My Profile"
                            ? "Manage your profile details."
                            : activePage === "Change Password"
                                ? "Update your account password."
                                : "Manage all your employees here."}
              </p>
            </div>

            <span>{role === "ADMIN" ? "Admin" : "Employee"}</span>
          </header>

          {/* Dashboard Page */}
          {activePage === "Dashboard" && (

              <section className="employee-section">

                <h2>Admin Dashboard</h2>

                <p>Welcome to Employee Management System</p>

                <div className="stats-container">

                  <div className="stat-card">
                    <h3>Total Users</h3>
                    <h2>{stats.totalUsers}</h2>
                  </div>

                  <div className="stat-card">
                    <h3>Total Employees</h3>
                    <h2>{stats.totalEmployees}</h2>
                  </div>

                  <div className="stat-card">
                    <h3>Total Departments</h3>
                    <h2>{stats.totalDepartments}</h2>
                  </div>

                </div>

              </section>
          )}

          {/* Employees Page */}
          {role === "ADMIN" && activePage === "Employees" && (

              <section className="employee-section">

                <div className="employee-heading">

                  <div>
                    <h2>Employee Directory</h2>
                    <p>Total Employees: {stats.totalEmployees}</p>
                  </div>

                  <button
                      className="add-btn"
                      onClick={() => {
                        setEditId(null);
                        setFormData({
                          name: "",
                          email: "",
                          phone: "",
                          department: "IT",
                        });
                        setShowForm(true);
                      }}
                  >
                    + Add Employee
                  </button>

                </div>

                {/* Statistics Cards */}
                <div className="stats-container">

                  <div className="stat-card">
                    <h3>Total Users</h3>
                    <h2>{stats.totalUsers}</h2>
                  </div>

                  <div className="stat-card">
                    <h3>Total Employees</h3>
                    <h2>{stats.totalEmployees}</h2>
                  </div>

                  <div className="stat-card">
                    <h3>Total Departments</h3>
                    <h2>{stats.totalDepartments}</h2>
                  </div>

                </div>
                <div className="chart-container">
                  <h3>Department-wise Employees</h3>

                  <div className="chart-bars">
                    {Object.entries(departmentCounts).map(([department, count]) => (
                        <div className="chart-item" key={department}>
                          <div className="chart-info">
                            <span>{department}</span>
                            <strong>{count}</strong>
                          </div>

                          <div className="bar-background">
                            <div
                                className="bar-fill"
                                style={{
                                  width: `${(count / Math.max(...Object.values(departmentCounts), 1)) * 100}%`,
                                }}
                            ></div>
                          </div>
                        </div>
                    ))}
                  </div>
                </div>

                {/* Add / Edit Form */}
                {showForm && (

                    <form
                        className="employee-form"
                        onSubmit={handleSubmit}
                    >

                      <h2>
                        {editId !== null
                            ? "Edit Employee"
                            : "Add New Employee"}
                      </h2>

                      <label>Full Name</label>

                      <input
                          name="name"
                          placeholder="Enter employee name"
                          value={formData.name}
                          onChange={handleChange}
                          required
                      />

                      <label>Email Address</label>

                      <input
                          type="email"
                          name="email"
                          placeholder="Enter email"
                          value={formData.email}
                          onChange={handleChange}
                          required
                      />

                      <label>Phone Number</label>

                      <input
                          type="tel"
                          name="phone"
                          placeholder="Enter phone number"
                          value={formData.phone}
                          onChange={handleChange}
                          pattern="[0-9]{10}"
                          title="Enter a valid 10 digit phone number"
                          required
                      />

                      <label>Department</label>

                      <select
                          name="department"
                          value={formData.department}
                          onChange={handleChange}
                      >
                        <option value="IT">IT</option>
                        <option value="HR">HR</option>
                        <option value="Finance">Finance</option>
                        <option value="Marketing">Marketing</option>
                        <option value="Sales">Sales</option>
                      </select>

                      <div className="form-buttons">

                        <button type="submit" className="add-btn">
                          Save Employee
                        </button>

                        <button
                            type="button"
                            className="cancel-btn"
                            onClick={() => {
                              setShowForm(false);
                              setEditId(null);
                            }}
                        >
                          Cancel
                        </button>

                      </div>

                    </form>
                )}

                {/* Search */}
                <input
                    className="search"
                    type="text"
                    placeholder="Search employee by name..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

                {/* Employee Table */}
                <div className="table-container">

                  <table>

                    <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Phone</th>
                      <th>Department</th>
                      <th>Actions</th>
                    </tr>
                    </thead>

                    <tbody>

                    {filteredEmployees.map((employee) => (

                        <tr key={employee.id}>

                          <td>{employee.id}</td>
                          <td>{employee.name}</td>
                          <td>{employee.email}</td>
                          <td>{employee.phone}</td>

                          <td>
                        <span className="department">
                          {employee.department}
                        </span>
                          </td>

                          <td>

                            <button
                                className="edit-btn"
                                onClick={() => handleEdit(employee)}
                            >
                              Edit
                            </button>

                            <button
                                className="delete-btn"
                                onClick={() => handleDelete(employee.id)}
                            >
                              Delete
                            </button>

                          </td>

                        </tr>

                    ))}

                    </tbody>

                  </table>

                  {filteredEmployees.length === 0 && (
                      <p className="empty">No employees found.</p>
                  )}

                </div>

              </section>
          )}

          {/* My Profile Page */}
          {activePage === "My Profile" && (
              <section className="employee-section">
                <h2>My Profile</h2>
                <p>Admin Account Information</p>

                {profile && (
                    <div className="profile-card">
                      <div className="profile-avatar">
                        {profile.userName?.charAt(0).toUpperCase()}
                      </div>

                      <h2>{profile.userName}</h2>
                      <span className="role-badge">ADMIN</span>

                      <div className="profile-details">
                        <div>
                          <strong>Username</strong>
                          <p>{profile.userName}</p>
                        </div>

                        <div>
                          <strong>Role</strong>
                          <p>{profile.role?.join(", ")}</p>
                        </div>

                        <div>
                          <strong>Employee Name</strong>
                          <p>{profile.employee?.name || "Not Available"}</p>
                        </div>

                        <div>
                          <strong>Email</strong>
                          <p>{profile.employee?.email || "Not Available"}</p>
                        </div>

                        <div>
                          <strong>Phone</strong>
                          <p>{profile.employee?.phone || "Not Available"}</p>
                        </div>

                        <div>
                          <strong>Department</strong>
                          <p>{profile.employee?.department || "Not Available"}</p>
                        </div>
                      </div>
                    </div>
                )}
                <button onClick={() => setActivePage("Change Password")}>
                  Change Password
                </button>

                {!profile && <p>Loading profile...</p>}
              </section>
          )}
          {/* Change Password Page */}
          {activePage === "Change Password" && (
              <section className="employee-section">
                <h2>Change Password</h2>
                <p>Update your account password</p>

                <form className="password-form" onSubmit={handleChangePassword}>
                  <div className="form-group">
                    <label>Current Password</label>
                    <input
                        type="password"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        placeholder="Enter current password"
                        required
                    />
                  </div>

                  <div className="form-group">
                    <label>New Password</label>
                    <input
                        type="password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        placeholder="Enter new password"
                        required
                    />
                  </div>

                  <div className="form-group">
                    <label>Confirm New Password</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Confirm new password"
                        required
                    />
                  </div>

                  <button type="submit" className="submit-btn">
                    Update Password
                  </button>
                </form>
              </section>
          )}


        </main>

      </div>
  );
}

export default App;
