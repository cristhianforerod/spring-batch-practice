package com.example.demospringbatch.listener;

import com.example.demospringbatch.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;



@Component
public class JobListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobListener.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JobListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("Finalizo el Job!!! Verifica los resultados: ");
            jdbcTemplate
                    .query("SELECT nombre, apellido, telefono FROM persona",
                            (rs, row) -> new Persona(rs.getString(1), rs.getString(2), rs.getString(3)))
                    .forEach(persona -> LOGGER.info("Registro <" + persona + ">"));
        }

    }
}
