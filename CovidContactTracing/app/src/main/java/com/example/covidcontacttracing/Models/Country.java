package com.example.covidcontacttracing.Models;

public class Country {
    private String country;
    private String confirmed;
    private String newConfirmed;
    private String active;
    private String death;
    private String newDeath;
    private String recovered;
    private String tests;
    private String flag;
    private String population;
    private String oneCasePerPeople;
    private String oneDeathPerPeople;
    private String oneTestPerPeople;

    public Country(String country, String confirmed, String newConfirmed, String active, String death, String newDeath, String recovered, String tests, String flag, String population, String oneCasePerPeople, String oneDeathPerPeople, String oneTestPerPeople) {
        this.country = country;
        this.confirmed = confirmed;
        this.newConfirmed = newConfirmed;
        this.active = active;
        this.death = death;
        this.newDeath = newDeath;
        this.recovered = recovered;
        this.tests = tests;
        this.flag = flag;
        this.population = population;
        this.oneCasePerPeople = oneCasePerPeople;
        this.oneDeathPerPeople = oneDeathPerPeople;
        this.oneTestPerPeople = oneTestPerPeople;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getOneCasePerPeople() {
        return oneCasePerPeople;
    }

    public void setOneCasePerPeople(String oneCasePerPeople) {
        this.oneCasePerPeople = oneCasePerPeople;
    }

    public String getOneDeathPerPeople() {
        return oneDeathPerPeople;
    }

    public void setOneDeathPerPeople(String oneDeathPerPeople) {
        this.oneDeathPerPeople = oneDeathPerPeople;
    }

    public String getOneTestPerPeople() {
        return oneTestPerPeople;
    }

    public void setOneTestPerPeople(String oneTestPerPeople) {
        this.oneTestPerPeople = oneTestPerPeople;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(String newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public String getNewDeath() {
        return newDeath;
    }

    public void setNewDeath(String newDeath) {
        this.newDeath = newDeath;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
