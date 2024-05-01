import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAandachtspunt } from 'app/shared/model/aandachtspunt.model';
import { getEntity, updateEntity, createEntity, reset } from './aandachtspunt.reducer';

export const AandachtspuntUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const aandachtspuntEntity = useAppSelector(state => state.aandachtspunt.entity);
  const loading = useAppSelector(state => state.aandachtspunt.loading);
  const updating = useAppSelector(state => state.aandachtspunt.updating);
  const updateSuccess = useAppSelector(state => state.aandachtspunt.updateSuccess);

  const handleClose = () => {
    navigate('/aandachtspunt');
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
      ...aandachtspuntEntity,
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
          ...aandachtspuntEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.aandachtspunt.home.createOrEditLabel" data-cy="AandachtspuntCreateUpdateHeading">
            Create or edit a Aandachtspunt
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
                <ValidatedField name="id" required readOnly id="aandachtspunt-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Code" id="aandachtspunt-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Naam" id="aandachtspunt-naam" name="naam" data-cy="naam" type="text" />
              <ValidatedField label="Omschrijving" id="aandachtspunt-omschrijving" name="omschrijving" data-cy="omschrijving" type="text" />
              <ValidatedField label="Actief" id="aandachtspunt-actief" name="actief" data-cy="actief" check type="checkbox" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aandachtspunt" replace color="info">
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

export default AandachtspuntUpdate;
