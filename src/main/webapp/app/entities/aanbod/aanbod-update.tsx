import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISubdoel } from 'app/shared/model/subdoel.model';
import { getEntities as getSubdoels } from 'app/entities/subdoel/subdoel.reducer';
import { IActiviteit } from 'app/shared/model/activiteit.model';
import { getEntities as getActiviteits } from 'app/entities/activiteit/activiteit.reducer';
import { IAanbod } from 'app/shared/model/aanbod.model';
import { getEntity, updateEntity, createEntity, reset } from './aanbod.reducer';

export const AanbodUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const subdoels = useAppSelector(state => state.subdoel.entities);
  const activiteits = useAppSelector(state => state.activiteit.entities);
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

    dispatch(getSubdoels({}));
    dispatch(getActiviteits({}));
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
      subdoels: mapIdList(values.subdoels),
      activiteits: mapIdList(values.activiteits),
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
          subdoels: aanbodEntity?.subdoels?.map(e => e.id.toString()),
          activiteits: aanbodEntity?.activiteits?.map(e => e.id.toString()),
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
              <ValidatedField label="Subdoelen" id="aanbod-subdoelen" name="subdoelen" data-cy="subdoelen" type="text" />
              <ValidatedField label="Subdoel" id="aanbod-subdoel" data-cy="subdoel" type="select" multiple name="subdoels">
                <option value="" key="0" />
                {subdoels
                  ? subdoels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Activiteit" id="aanbod-activiteit" data-cy="activiteit" type="select" multiple name="activiteits">
                <option value="" key="0" />
                {activiteits
                  ? activiteits.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
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
