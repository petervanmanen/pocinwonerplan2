import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOntwikkelwens } from 'app/shared/model/ontwikkelwens.model';
import { getEntity, updateEntity, createEntity, reset } from './ontwikkelwens.reducer';

export const OntwikkelwensUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ontwikkelwensEntity = useAppSelector(state => state.ontwikkelwens.entity);
  const loading = useAppSelector(state => state.ontwikkelwens.loading);
  const updating = useAppSelector(state => state.ontwikkelwens.updating);
  const updateSuccess = useAppSelector(state => state.ontwikkelwens.updateSuccess);

  const handleClose = () => {
    navigate('/ontwikkelwens');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...ontwikkelwensEntity,
      ...values,
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
          ...ontwikkelwensEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.ontwikkelwens.home.createOrEditLabel" data-cy="OntwikkelwensCreateUpdateHeading">
            Create or edit a Ontwikkelwens
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
                <ValidatedField name="id" required readOnly id="ontwikkelwens-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Code" id="ontwikkelwens-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Naam" id="ontwikkelwens-naam" name="naam" data-cy="naam" type="text" />
              <ValidatedField label="Omschrijving" id="ontwikkelwens-omschrijving" name="omschrijving" data-cy="omschrijving" type="text" />
              <ValidatedField label="Actief" id="ontwikkelwens-actief" name="actief" data-cy="actief" check type="checkbox" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ontwikkelwens" replace color="info">
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

export default OntwikkelwensUpdate;
