package com.covid.repository;

import com.covid.entity.CovidPageVisitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CovidPageVisitorRepository extends JpaRepository<CovidPageVisitor,Integer>{

}