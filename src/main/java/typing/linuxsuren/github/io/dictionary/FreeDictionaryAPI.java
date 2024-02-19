/*
Copyright 2024 LinuxSuRen.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package typing.linuxsuren.github.io.dictionary;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class FreeDictionaryAPI implements DictionaryService{
    @Override
    public Vocabulary query(String word) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.dictionaryapi.dev/api/v2/entries/en/" + word))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = null;
            if (response.statusCode() == 200) {
                body = response.body();
            } else {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            TypeFactory typeFactory = mapper.getTypeFactory();
            List<Word> words = mapper.readValue(body, typeFactory.constructCollectionType(List.class, Word.class));
            if (words != null && !words.isEmpty()) {
                Word mean = words.get(0);
                if (mean.getMeanings() != null && !mean.getMeanings().isEmpty()) {
                    List<Definition> defs = mean.getMeanings().get(0).getDefinitions();
                    if (defs != null && !defs.isEmpty()) {
                        Vocabulary vol = new Vocabulary();
                        vol.setWord(word);
                        vol.setMeaning(defs.get(0).getDefinition());
                        return vol;
                    }
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    static class Word {
        private List<Mean> meanings;

        public List<Mean> getMeanings() {
            return meanings;
        }

        public void setMeanings(List<Mean> meanings) {
            this.meanings = meanings;
        }
    }

    static class Mean {
        private List<Definition> definitions;

        public List<Definition> getDefinitions() {
            return definitions;
        }

        public void setDefinitions(List<Definition> definitions) {
            this.definitions = definitions;
        }
    }

    static class Definition {
        private String definition;

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }
    }
}
