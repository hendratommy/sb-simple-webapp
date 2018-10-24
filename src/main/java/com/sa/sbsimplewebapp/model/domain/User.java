package com.sa.sbsimplewebapp.model.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Audited
@Table(name="tbl_users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", precision = 20)
    private Long id;
    @Size(min = 1)
    @NotNull
    @Column(name = "username", length = 50, unique = true)
    private String username;
    @XmlTransient
    @JsonIgnore
    @Size(min = 1)
    @NotNull
    @Column(name = "password", length = 64)
    private String password;
    @Column(name = "name", length = 100)
    private String name;
    @NotNull
    @Column(name = "email", length = 100)
    private String email;
    @NotNull
    @Column(name = "enabled")
    private boolean enabled = true;
    @NotNull
    @Column(name = "locked")
    private boolean locked = false;
    @XmlTransient
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<Role>();

    // versioning
    @Version
    @Column(name = "version", precision = 20, nullable = false, columnDefinition = "bigint default 0")
    private Long version = 0L;

    // audit
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    @CreatedDate
    private Calendar createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    @LastModifiedDate
    private Calendar updatedDate;
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;
    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    @XmlTransient
    public List<String> getRolesValue() {
        List<String> list = new ArrayList<>();
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                list.add(role.getName());
            }
        }
        return list;
    }

    @JsonProperty("roles")
    public List<Map<String, Object>> getRolesAsKeyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", role.getId());
                map.put("name", role.getName());
                list.add(map);
            }
        }
        return list;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public Calendar getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void addRole(Role role) {
        if (!this.roles.contains(role))
            this.roles.add(role);
    }

    public void removeRole(Role role) {
        if (this.roles.contains(role)) {
            roles.remove(role);
            role.removeUser(this);
        }
    }

    public void mergeRoles(List<Role> roles) {
        List<Role> currentRoles = new ArrayList<Role>(this.roles);
        for (Role currentRole : currentRoles) {
            if (!roles.contains(currentRole)) {
                removeRole(currentRole);
            }
        }
        for (Role role : roles) {
            addRole(role);
        }
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return id != null && Objects.equals(id, other.getId());
    }

}
