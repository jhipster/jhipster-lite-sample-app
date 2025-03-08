package tech.jhipster.lite.sample.sample.infrastructure.primary.beer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.lite.sample.sample.application.BeersApplicationService;
import tech.jhipster.lite.sample.sample.domain.BeerId;

@RestController
@Tag(name = "Beers")
@RequestMapping("/api/beers")
class BeersResource {

  private final BeersApplicationService beers;

  public BeersResource(BeersApplicationService beers) {
    this.beers = beers;
  }

  @PostMapping
  @Operation(summary = "Add a beer to the catalog")
  @ApiResponse(description = "Beer added to the catalog", responseCode = "201")
  ResponseEntity<RestBeer> addBeer(@Validated @RequestBody RestBeerToCreate beerToCreate) {
    var createdBeer = beers.create(beerToCreate.toDomain());

    return new ResponseEntity<>(RestBeer.from(createdBeer), HttpStatus.CREATED);
  }

  @GetMapping
  @Operation(summary = "Get all available beers")
  @ApiResponse(description = "Beers catalog", responseCode = "200")
  ResponseEntity<RestBeers> getBeers() {
    return ResponseEntity.ok(RestBeers.from(beers.catalog()));
  }

  @DeleteMapping("/{beer-id}")
  @Operation(summary = "Remove beer from catalog")
  @ApiResponse(description = "Beer removed from the catalog", responseCode = "200")
  void removeBeer(@PathVariable("beer-id") UUID id) {
    beers.remove(new BeerId(id));
  }
}
