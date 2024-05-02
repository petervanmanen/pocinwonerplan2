import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAandachtspunt } from 'app/shared/model/aandachtspunt.model';
import { getEntities as getAandachtspunts } from 'app/entities/aandachtspunt/aandachtspunt.reducer';
import { IOntwikkelwens } from 'app/shared/model/ontwikkelwens.model';
import { getEntities as getOntwikkelwens } from 'app/entities/ontwikkelwens/ontwikkelwens.reducer';
import { IInwonerplan } from 'app/shared/model/inwonerplan.model';
import { getEntities as getInwonerplans } from 'app/entities/inwonerplan/inwonerplan.reducer';
import { IInwonerplanSubDoel } from 'app/shared/model/inwonerplan-sub-doel.model';
import { getEntity, updateEntity, createEntity, reset } from './inwonerplan-sub-doel.reducer';

export const InwonerplanSubDoelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const aandachtspunts = useAppSelector(state => state.aandachtspunt.entities);
  const ontwikkelwens = useAppSelector(state => state.ontwikkelwens.entities);
  const inwonerplans = useAppSelector(state => state.inwonerplan.entities);
  const inwonerplanSubDoelEntity = useAppSelector(state => state.inwonerplanSubDoel.entity);
  const loading = useAppSelector(state => state.inwonerplanSubDoel.loading);
  const updating = useAppSelector(state => state.inwonerplanSubDoel.updating);
  const updateSuccess = useAppSelector(state => state.inwonerplanSubDoel.updateSuccess);

  const handleClose = () => {
    navigate('/inwonerplan-sub-doel');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAandachtspunts({}));
    dispatch(getOntwikkelwens({}));
    dispatch(getInwonerplans({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.code !== undefined && typeof values.code !== 'number') {
      values.code = Number(values.code);
    }

    const entity = {
      ...inwonerplanSubDoelEntity,
      ...values,
      aandachtspunt: aandachtspunts.find(it => it.id.toString() === values.aandachtspunt?.toString()),
      ontwikkelwens: ontwikkelwens.find(it => it.id.toString() === values.ontwikkelwens?.toString()),
      inwonerplan: inwonerplans.find(it => it.id.toString() === values.inwonerplan?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...inwonerplanSubDoelEntity,
          aandachtspunt: inwonerplanSubDoelEntity?.aandachtspunt?.id,
          ontwikkelwens: inwonerplanSubDoelEntity?.ontwikkelwens?.id,
          inwonerplan: inwonerplanSubDoelEntity?.inwonerplan?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.inwonerplanSubDoel.home.createOrEditLabel" data-cy="InwonerplanSubDoelCreateUpdateHeading">
            Create or edit a Inwonerplan Sub Doel
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="inwonerplan-sub-doel-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Code" id="inwonerplan-sub-doel-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Naam" id="inwonerplan-sub-doel-naam" name="naam" data-cy="naam" type="text" />
              <ValidatedField label="Actief" id="inwonerplan-sub-doel-actief" name="actief" data-cy="actief" check type="checkbox" />
              <ValidatedField
                id="inwonerplan-sub-doel-aandachtspunt"
                name="aandachtspunt"
                data-cy="aandachtspunt"
                label="Aandachtspunt"
                type="select"
              >
                <option value="" key="0" />
                {aandachtspunts
                  ? aandachtspunts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inwonerplan-sub-doel-ontwikkelwens"
                name="ontwikkelwens"
                data-cy="ontwikkelwens"
                label="Ontwikkelwens"
                type="select"
              >
                <option value="" key="0" />
                {ontwikkelwens
                  ? ontwikkelwens.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="inwonerplan-sub-doel-inwonerplan"
                name="inwonerplan"
                data-cy="inwonerplan"
                label="Inwonerplan"
                type="select"
              >
                <option value="" key="0" />
                {inwonerplans
                  ? inwonerplans.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/inwonerplan-sub-doel" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default InwonerplanSubDoelUpdate;
