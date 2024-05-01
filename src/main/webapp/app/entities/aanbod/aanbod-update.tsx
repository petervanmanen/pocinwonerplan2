import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActiviteit } from 'app/shared/model/activiteit.model';
import { getEntities as getActiviteits } from 'app/entities/activiteit/activiteit.reducer';
import { IAandachtspunt } from 'app/shared/model/aandachtspunt.model';
import { getEntities as getAandachtspunts } from 'app/entities/aandachtspunt/aandachtspunt.reducer';
import { IOntwikkelwens } from 'app/shared/model/ontwikkelwens.model';
import { getEntities as getOntwikkelwens } from 'app/entities/ontwikkelwens/ontwikkelwens.reducer';
import { IAanbod } from 'app/shared/model/aanbod.model';
import { getEntity, updateEntity, createEntity, reset } from './aanbod.reducer';

export const AanbodUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activiteits = useAppSelector(state => state.activiteit.entities);
  const aandachtspunts = useAppSelector(state => state.aandachtspunt.entities);
  const ontwikkelwens = useAppSelector(state => state.ontwikkelwens.entities);
  const aanbodEntity = useAppSelector(state => state.aanbod.entity);
  const loading = useAppSelector(state => state.aanbod.loading);
  const updating = useAppSelector(state => state.aanbod.updating);
  const updateSuccess = useAppSelector(state => state.aanbod.updateSuccess);

  const handleClose = () => {
    navigate('/aanbod');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getActiviteits({}));
    dispatch(getAandachtspunts({}));
    dispatch(getOntwikkelwens({}));
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

    const entity = {
      ...aanbodEntity,
      ...values,
      activiteits: mapIdList(values.activiteits),
      aandachtspunts: mapIdList(values.aandachtspunts),
      ontwikkelwens: mapIdList(values.ontwikkelwens),
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
          ...aanbodEntity,
          activiteits: aanbodEntity?.activiteits?.map(e => e.id.toString()),
          aandachtspunts: aanbodEntity?.aandachtspunts?.map(e => e.id.toString()),
          ontwikkelwens: aanbodEntity?.ontwikkelwens?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.aanbod.home.createOrEditLabel" data-cy="AanbodCreateUpdateHeading">
            Create or edit a Aanbod
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="aanbod-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Naam" id="aanbod-naam" name="naam" data-cy="naam" type="text" />
              <ValidatedField label="Activiteit" id="aanbod-activiteit" data-cy="activiteit" type="select" multiple name="activiteits">
                <option value="" key="0" />
                {activiteits
                  ? activiteits.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id} {otherEntity.naam}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Aandachtspunt"
                id="aanbod-aandachtspunt"
                data-cy="aandachtspunt"
                type="select"
                multiple
                name="aandachtspunts"
              >
                <option value="" key="0" />
                {aandachtspunts
                  ? aandachtspunts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id} {otherEntity.naam}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Ontwikkelwens"
                id="aanbod-ontwikkelwens"
                data-cy="ontwikkelwens"
                type="select"
                multiple
                name="ontwikkelwens"
              >
                <option value="" key="0" />
                {ontwikkelwens
                  ? ontwikkelwens.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id} {otherEntity.naam}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aanbod" replace color="info">
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

export default AanbodUpdate;
