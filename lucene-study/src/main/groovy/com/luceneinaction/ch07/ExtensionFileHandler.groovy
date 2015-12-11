package com.luceneinaction.ch07

import groovy.transform.TypeChecked

import org.apache.lucene.document.Document

@TypeChecked
class ExtensionFileHandler implements FileHandler {
	private Properties handlerProps

	public ExtensionFileHandler(Properties props) {
		this.handlerProps = props
	}

	@Override
	public Document getDocument(File file) throws FileHandlerException {
		def doc = null
		def name = file.getName()
		def dotIndex = name.indexOf(".")
		if ((dotIndex > 0) && (dotIndex < name.size())) {
			def ext = name.substring(dotIndex + 1, name.size())
			def handlerClassName = handlerProps.getProperty(ext)

			try {
				def handlerClass = Class.forName(handlerClassName)
				def handler = (DocumentHandler) handlerClass.newInstance()
				return handler.getDocument(new FileInputStream(file))
			} catch (Exception e) {
				throw new FileHandlerException("cannot create instance", e)
			}
		}
		return null
	}

	static main(args) {
		def argList = args.collect()
		if (argList.size() < 2) {
			println "invalid arguments"
			System.exit(0)
		}

		def props = new Properties()
		props.load(new FileInputStream(new File(argList.getAt(0) as String)))
		
		def fileHandler = new ExtensionFileHandler(props)
		def doc = fileHandler.getDocument(new File(argList.getAt(1) as String))
	}
}
