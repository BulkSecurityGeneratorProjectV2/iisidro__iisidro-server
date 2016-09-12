package ar.edu.utn.frro.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.utn.frro.domain.Seccion;
import ar.edu.utn.frro.repository.SeccionRepository;
import ar.edu.utn.frro.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Seccion.
 */
@RestController
@RequestMapping("/api")
public class SeccionResource {

    private final Logger log = LoggerFactory.getLogger(SeccionResource.class);

    @Inject
    private SeccionRepository seccionRepository;

    /**
     * POST  /secciones : Create a new seccion.
     *
     * @param seccion the seccion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new seccion, or with status 400 (Bad Request) if the seccion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/secciones",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seccion> createSeccion(@Valid @RequestBody Seccion seccion) throws URISyntaxException {
        log.debug("REST request to save Seccion : {}", seccion);
        if (seccion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("seccion", "idexists", "A new seccion cannot already have an ID")).body(null);
        }
        Seccion result = seccionRepository.save(seccion);
        return ResponseEntity.created(new URI("/api/secciones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("seccion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /secciones : Updates an existing seccion.
     *
     * @param seccion the seccion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated seccion,
     * or with status 400 (Bad Request) if the seccion is not valid,
     * or with status 500 (Internal Server Error) if the seccion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/secciones",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seccion> updateSeccion(@Valid @RequestBody Seccion seccion) throws URISyntaxException {
        log.debug("REST request to update Seccion : {}", seccion);
        if (seccion.getId() == null) {
            return createSeccion(seccion);
        }
        Seccion result = seccionRepository.save(seccion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("seccion", seccion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /secciones : get all the secciones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of secciones in body
     */
    @RequestMapping(value = "/secciones",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Seccion> getAllSecciones() {
        log.debug("REST request to get all secciones");
        List<Seccion> secciones = seccionRepository.findAll();
        return secciones;
    }

    /**
     * GET  /secciones : get all the secciones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of secciones in body
     */
    @RequestMapping(value = "/seccionesByEncuesta/{encuestaId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Seccion> getAllSeccionesByEncuesta(@PathVariable Long encuestaId) {
        log.debug("REST request to get all secciones by encuesta id {}", encuestaId);
        List<Seccion> secciones = seccionRepository.findAllByEncuesta(encuestaId);
        return secciones;
    }

    /**
     * GET  /secciones/:id : get the "id" seccion.
     *
     * @param id the id of the seccion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the seccion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/secciones/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Seccion> getSeccion(@PathVariable Long id) {
        log.debug("REST request to get Seccion : {}", id);
        Seccion seccion = seccionRepository.findOne(id);
        return Optional.ofNullable(seccion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /secciones/:id : delete the "id" seccion.
     *
     * @param id the id of the seccion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/secciones/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSeccion(@PathVariable Long id) {
        log.debug("REST request to delete Seccion : {}", id);
        seccionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("seccion", id.toString())).build();
    }

}