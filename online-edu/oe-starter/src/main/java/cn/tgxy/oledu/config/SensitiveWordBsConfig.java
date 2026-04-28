package cn.tgxy.oledu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.check.WordChecks;
import com.github.houbb.sensitive.word.support.ignore.SensitiveWordCharIgnores;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import com.github.houbb.sensitive.word.support.tag.WordTags;

@Configuration
public class SensitiveWordBsConfig {

	@Bean("sensitiveWordBs")
	SensitiveWordBs sensitiveWordBs() {
		return SensitiveWordBs.newInstance()
				.ignoreCase(true).ignoreWidth(false).ignoreNumStyle(false).ignoreChineseStyle(true).ignoreEnglishStyle(false).ignoreRepeat(false)
				.enableNumCheck(false).enableEmailCheck(false).enableUrlCheck(false).enableIpv4Check(false).enableWordCheck(true)
				// .wordCheckNum(WordChecks.num())
				// .wordCheckEmail(WordChecks.email())
				// .wordCheckIpv4(WordChecks.ipv4())
				.wordCheckUrl(WordChecks.urlNoPrefix()).wordCheckWord(WordChecks.word()).wordTag(WordTags.none()).wordResultCondition(WordResultConditions.alwaysTrue())
				.numCheckLen(8)
				.charIgnore(SensitiveWordCharIgnores.defaults())
				.init();
	}

}
