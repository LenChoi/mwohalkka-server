package com.wedo.mwohalkka.core.controller

import com.wedo.mwohalkka.core.grid.GridReq
import com.wedo.mwohalkka.core.grid.GridRes
import com.wedo.mwohalkka.core.repository.BaseRepository
import com.wedo.mwohalkka.core.service.GenericService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.SessionStatus
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.util.*
import javax.annotation.PostConstruct
import javax.validation.Valid

open abstract class GenericController<E, P : Serializable, D, RQ : GridReq<E>, RS : GridRes<D>, S : GenericService<E, P, out BaseRepository<E, P>>> {
    protected val CLOSE_AND_REFRESH_VIEW = "closeandrefresh"
    protected val CLOSE_AND_RELOAD_VIEW = "closeandreload"
    protected val ADD_MSG = 1
    protected val EDIT_MSG = 2
    protected val DELETE_MSG = 3
    protected val CUSTOM_MSG = 9
    private val log = KotlinLogging.logger {}

    @Autowired
    protected lateinit var ac: ApplicationContext

    @Autowired
    protected lateinit var service: S

    @Autowired
    protected lateinit var mapper: ModelMapper

    @Autowired(required = false)
    lateinit var messageSource: MessageSource

    protected var entityClass: Class<E>? = null
    protected var gridResClass: Class<RS>? = null
    protected var dataClass: Class<D>? = null
    private var defaultModuleName: String? = null
    private var defaultModelName: String? = null

    open fun moduleName(): String? {
        return defaultModuleName
    }

    fun message(key: String): String {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale())
    }

    open fun message(key: String, args: Array<Any>): String {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale())
    }

    open fun modelName(): String? {
        return defaultModelName
    }

    open fun prefix(): String? {
        return ""
    }

    open fun suffix(): String? {
        return ""
    }

    @PostConstruct
    fun initInternal() {
        val actualTypeArguments = (
            javaClass
                .genericSuperclass as ParameterizedType
            ).actualTypeArguments
        entityClass = actualTypeArguments[0] as Class<E>
        dataClass = actualTypeArguments[2] as Class<D>
        gridResClass = actualTypeArguments[4] as Class<RS>
        val entityName = entityClass!!.simpleName
        defaultModuleName = entityName.lowercase(Locale.getDefault())
        val var10001 = entityName.substring(0, 1).lowercase(Locale.getDefault())
        defaultModelName = var10001 + entityName.substring(1)
    }

    open fun mainReferenceData(model: Model) {}

    @RequestMapping
    open fun main(model: Model): String {
        this.mainReferenceData(model)
        val var10000 = prefix()
        return var10000 + moduleName() + suffix() + "/" + moduleName() + "main"
    }

    @GetMapping("/grid")
    @ResponseBody
    open fun grid(req: RQ): GridRes<D> {
        val codes = service.search(req)
        return convertToData(req, codes)
    }

    protected open fun convertToData(req: RQ, list: List<E>): GridRes<D> {
        var res: GridRes<D> = try {
            gridResClass!!.newInstance() as GridRes<D>
        } catch (var9: Exception) {
            throw RuntimeException(var9)
        }

        res.total = req.getTotalPages()
        res.page = req.getPage()
        res.records = req.getRecords()
        val data = mutableListOf<D>()
        var i = 0
        val var6: Iterator<E> = list.iterator()
        while (var6.hasNext()) {
            val c: E = var6.next()
            val dt = mapper.map(c, dataClass)
            convertMore(c, dt)
            data.add(i, dt)
            ++i
        }
        res.rows = data
        return res
    }

    open fun convertMore(entity: E, data: D) {}

    @RequestMapping(value = ["/add"], method = [RequestMethod.GET])
    open fun addForm(model: Model): String? {
        val entity: E = this.createEntity()
        this.defaults(entity)
        model.addAttribute(modelName()!!, entity)
        this.formReferenceData(model, entity)
        this.addReferenceData(model, entity)
        return this.addFormView()
    }

    @RequestMapping(value = ["/add"], method = [RequestMethod.POST])
    open fun addSubmit(
        @ModelAttribute @Valid entity: E,
        errors: BindingResult,
        status: SessionStatus,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        log.debug("submit :", entity)
        validate(entity, errors)
        validateAdd(entity, errors)
        return if (errors.hasErrors()) {
            formReferenceData(model, entity)
            addReferenceData(model, entity)
            addFormView()
        } else {
            status.setComplete()
            addEntity(entity)
            addSuccessView(entity, model, redirectAttributes)
        }
    }

    protected open fun addEntity(entity: E) {
        service.add(entity)
    }

    protected open fun addSuccessView(entity: E, model: Model, redirectAttributes: RedirectAttributes): String {
        model.addAttribute("msg", ADD_MSG)
        return "closeandrefresh"
    }

    open fun addReferenceData(model: Model, entity: E) {}

    open fun formReferenceData(model: Model, entity: E) {}

    open fun defaults(entity: E) {}

    protected open fun createEntity(): E {
        return try {
            entityClass!!.newInstance()
        } catch (var2: Exception) {
            throw RuntimeException(var2)
        }
    }

    protected open fun addFormView(): String {
        val var10000 = prefix()
        return var10000 + moduleName() + suffix() + "/" + moduleName() + "add"
    }

    open fun validate(entity: E, errors: BindingResult) {}

    open fun validateAdd(entity: E, errors: BindingResult?) {}

    @GetMapping("/{id}/edit")
    open fun editForm(@PathVariable id: P, model: Model): String? {
        val entity = service.find(id)
        model.addAttribute(modelName()!!, entity)
        formReferenceData(model, entity)
        editReferenceData(model, entity)
        return this.editFormView()
    }

    protected open fun editReferenceData(model: Model, entity: E) {}

    @PostMapping(value = ["/{id}/edit"])
    open fun editSubmit(
        @PathVariable id: P,
        @ModelAttribute @Valid entity: E,
        errors: BindingResult,
        status: SessionStatus,
        model: Model,
        ra: RedirectAttributes
    ): String {
        validate(entity, errors)
        validateEdit(entity, errors)
        return if (errors.hasErrors()) {
            formReferenceData(model, entity)
            editReferenceData(model, entity)
            editFormView()
        } else {
            status.setComplete()
            editEntity(entity)
            editSuccessView(entity, model, ra)
        }
    }

    open fun editEntity(entity: E) {
        service.update(entity)
    }

    protected open fun editFormView(): String {
        val var10000 = prefix()
        return var10000 + moduleName() + suffix() + "/" + moduleName() + "edit"
    }

    open fun validateEdit(entity: E, errors: BindingResult) {}

    protected open fun editSuccessView(entity: E, model: Model, redirectAttributes: RedirectAttributes): String {
        model.addAttribute("msg", EDIT_MSG)
        return "closeandrefresh"
    }

    @GetMapping("/{id}/delete")
    open fun delete(@PathVariable id: P, model: Model): String {
        this.service.deleteById(id)
        return this.deleteSuccessView(id, model)
    }

    protected open fun deleteSuccessView(id: P, model: Model): String {
        model.addAttribute("msg", DELETE_MSG)
        return "closeandrefresh"
    }
}
