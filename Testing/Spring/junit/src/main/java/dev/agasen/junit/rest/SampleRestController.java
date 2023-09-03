package dev.agasen.junit.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {

  private final static Map<String, String> countries = Map.of(
    "PH", "Philippines",
    "CA", "Canada",
    "JP", "Japan"
  );

  @GetMapping("/countries")
  public List<String> getCountries() {
    return new ArrayList<>(countries.values());
  }

  @GetMapping("/countries/{tag}")
  public String getCountryByTag(@PathVariable String tag) {
    String country = countries.get(tag);
    if (country == null) throw new RuntimeException();
    return country;
  }
}
