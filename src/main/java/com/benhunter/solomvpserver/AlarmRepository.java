package com.benhunter.solomvpserver;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:3000")
public interface AlarmRepository extends CrudRepository<Alarm, Long> {
}
