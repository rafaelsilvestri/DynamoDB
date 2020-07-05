package com.github.rafaelsilvestri.dynamodb.customer;

import java.util.UUID;

/**
 * Domain Entity
 *
 * @author Rafael Silvestri
 */
public class Customer {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    public Customer() {
        // Default constructor is required by AWS DynamoDB SDK
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
