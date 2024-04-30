package io.isiflix.ai.langchindemo.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel.OpenAiChatModelBuilder;
import dev.langchain4j.model.openai.OpenAiImageModel.OpenAiImageModelBuilder;
import io.isiflix.ai.langchindemo.dto.MyQuestion;
import io.isiflix.ai.langchindemo.dto.MyStructuredTemplate;
import io.isiflix.ai.langchindemo.dto.MyStructuredTemplate.PromptDeReceita;

@RestController
public class OpenAIController {

	@Value("${OPENAI_KEY_FREE}")
	private String apiKey;

	@Autowired
	private ChatLanguageModel chatModel;

	@PostMapping("/answer")
	public String chatWithOpenAI(@RequestBody MyQuestion question) {
		ChatLanguageModel customModel = new OpenAiChatModelBuilder().apiKey(apiKey).modelName("gpt-3.5-turbo")
				.temperature(0.1).build();
		return customModel.generate(question.question());

	}

	@GetMapping("/receita")
	public String facaUmaReceita() {
		MyStructuredTemplate template = new MyStructuredTemplate();
		PromptDeReceita rcPrompt = new PromptDeReceita();
		rcPrompt.prato = "Assado";
		rcPrompt.ingredientes = Arrays.asList("carne", "tomate", "cebola", "pimentao");

		Prompt prompt = StructuredPromptProcessor.toPrompt(rcPrompt);

		return chatModel.generate(prompt.text());
	}

	@PostMapping("/imagem")
	public String generateImage(@RequestBody MyQuestion question) {
		try {
			ImageModel imageModel = new OpenAiImageModelBuilder().apiKey(apiKey).modelName("dall-e").build();
			return imageModel.generate(question.question()).content().url().toURL().toString();
		} catch (Exception ex) {
			return null;
		}

	}

}
