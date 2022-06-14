package com.wedo.mwohalkka.core.controller

import com.wedo.mwohalkka.core.grid.GridReq
import com.wedo.mwohalkka.core.grid.GridRes
import com.wedo.mwohalkka.core.repository.BaseRepository
import com.wedo.mwohalkka.core.service.GenericService
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.support.SessionStatus
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.Serializable
import javax.validation.Valid

/**
 * Subgrid(edit이나 add화면에 나오는 그리드)용 Generic Web.
 *
 * 1. URL 구조
 * - edit/{ownerid}/$model/grid
 * - edit/{ownerid}/$model/subadd	- url이 다름
 * - edit/{ownerid}/$model/subedit	- url이 다름
 * - edit/{ownerid}/$model/delete  - GenericWeb과 동일
 *
 * 2. ownerName()
 *
 * owner의 model명
 *
 * 3. addSubEntity, editSubEntity
 *
 * ownerId를 이용해서 추가 또는 수정 기능 작성
 *
 * @author Toby
 *
 * @param <E>
 * @param <P>
 * @param <D>
 * @param <RQ>
 * @param <RS>
 * @param <S>
</S></RS></RQ></D></P></E> */
abstract class GenericSubController<E, // 0
        P : Serializable, // 1
        D, // 2
        RQ : GridReq<E>, // 3
        RS : GridRes<D>, // 4
        S : GenericService<E, P, out BaseRepository<E, P>>> : GenericController<E, P, D, RQ, RS, S>() {
    /**
     * owner(main) 모듈 이름
     * @return
     */
    protected abstract fun ownerName(): String

    /** ======================== ADD ======================  */
    fun addform(model: Model): String {
        throw UnsupportedOperationException("use '/subadd' instead")
    }

    fun addsubmit(
        entity: E,
        errors: BindingResult,
        status: SessionStatus,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        throw UnsupportedOperationException("use '/subadd' instead")
    }

    override fun addEntity(entity: E) {
        throw UnsupportedOperationException()
    }

    @RequestMapping(value = ["/subadd"], method = [RequestMethod.GET])
    fun addform(@PathVariable("ownerid") ownerid: P, model: Model): String {
        val entity: E = createEntity()
        defaults(entity)
        model.addAttribute(modelName()!!, entity)
        formReferenceData(model, entity, ownerid)
        addReferenceData(model, entity, ownerid)
        return addFormView()
    }

    override fun addFormView(): String {
        return prefix() + ownerName() + "/sub" + moduleName() + "add"
    }

    @RequestMapping(value = ["/subadd"], method = [RequestMethod.POST])
    fun addsubmit(
        @PathVariable("ownerid") ownerid: P,
        @ModelAttribute entity: @Valid E,
        errors: BindingResult,
        status: SessionStatus,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        validate(entity, errors)
        validateAdd(entity, errors)
        if (errors.hasErrors()) {
            formReferenceData(model, entity, ownerid)
            addReferenceData(model, entity, ownerid)
            return addFormView()
        }
        status.setComplete()
        addSubEntity(ownerid, entity)
        return addSuccessView(entity, model, redirectAttributes)
    }

    protected abstract fun addSubEntity(ownerid: P, entity: E)

    /** ======================== UPDATE ======================  */
    fun editform(id: P, model: Model): String {
        throw UnsupportedOperationException("use '/subedit' instead")
    }

    fun editsubmit(
        id: P,
        entity: E,
        errors: BindingResult,
        status: SessionStatus,
        model: Model,
        ra: RedirectAttributes
    ): String {
        throw UnsupportedOperationException("use '/subedit' instead")
    }

    override fun editEntity(entity: E) {
        throw UnsupportedOperationException()
    }

    @RequestMapping(value = ["/{id}/subedit"])
    fun editform(@PathVariable("ownerid") ownerid: P, @PathVariable id: P, model: Model): String {
        val entity = service.find(id)
        model.addAttribute(modelName()!!, entity)
        formReferenceData(model, entity, ownerid)
        editReferenceData(model, entity, ownerid)
        return editFormView()
    }

    override fun editFormView(): String {
        return prefix() + ownerName() + "/sub" + moduleName() + "edit"
    }

    @RequestMapping(value = ["/{id}/subedit"], method = [RequestMethod.POST])
    fun editsubmit(
        @PathVariable("ownerid") ownerid: P,
        id: P,
        @ModelAttribute entity: @Valid E,
        errors: BindingResult,
        status: SessionStatus,
        model: Model,
        ra: RedirectAttributes
    ): String {
        validate(entity, errors)
        validateEdit(entity, errors)
        if (errors.hasErrors()) {
            formReferenceData(model, entity, ownerid)
            editReferenceData(model, entity, ownerid)
            return editFormView()
        }
        status.setComplete()
        editSubEntity(ownerid, entity)
        return editSuccessView(entity, model, ra)
    }

    protected abstract fun editSubEntity(ownerid: P, entity: E)

    /** ======================== VALIDATION ======================  */
    protected fun formReferenceData(model: Model, entity: E, ownerid: P) {
        super.formReferenceData(model, entity)
    }

    protected fun addReferenceData(model: Model, entity: E, ownerid: P) {
        super.addReferenceData(model, entity)
    }

    protected fun editReferenceData(model: Model, entity: E, ownerid: P) {
        super.editReferenceData(model, entity)
    }
}
