/**
 * Copyright 2011-2012 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.charts.template

import org.fusesource.scalate.TemplateEngine

import com.excilys.ebi.gatling.charts.component.Component
import com.excilys.ebi.gatling.charts.config.ChartsFiles.ALL_SESSIONS_FILE
import com.excilys.ebi.gatling.charts.config.ChartsFiles.GATLING_JS_FILE
import com.excilys.ebi.gatling.charts.config.ChartsFiles.GATLING_TEMPLATE_LAYOUT_FILE_URL
import com.excilys.ebi.gatling.charts.config.ChartsFiles.JQUERY_FILE
import com.excilys.ebi.gatling.charts.config.ChartsFiles.STATS_JS_FILE
import com.excilys.ebi.gatling.core.result.message.RunRecord

object PageTemplate {
	val TEMPLATE_ENGINE = {
		val engine = new TemplateEngine
		engine.allowReload = false
		engine.escapeMarkup = false
		engine
	}

	private var runRecord: RunRecord = _
	def setRunInfo(runRecord: RunRecord) { PageTemplate.runRecord = runRecord }
}

abstract class PageTemplate(title: String, isDetails: Boolean, components: Component*) {

	val jsFiles: Seq[String] = (Seq(JQUERY_FILE, GATLING_JS_FILE, ALL_SESSIONS_FILE, STATS_JS_FILE) ++ getAdditionnalJSFiles).distinct

	def getContent: String = components.map(_.getHTMLContent).mkString

	def getJavascript: String = components.map(_.getJavascriptContent).mkString

	def getAdditionnalJSFiles: Seq[String] = components.flatMap(_.getJavascriptFiles)

	def getAttributes: Map[String, Any] =
		Map("jsFiles" -> jsFiles,
			"pageTitle" -> title,
			"pageContent" -> getContent,
			"javascript" -> getJavascript,
			"isDetails" -> isDetails,
			"runRecord" -> PageTemplate.runRecord)

	def getOutput: String = {
		PageTemplate.TEMPLATE_ENGINE.layout(GATLING_TEMPLATE_LAYOUT_FILE_URL, getAttributes)
	}
}