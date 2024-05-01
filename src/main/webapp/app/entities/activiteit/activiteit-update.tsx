import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAanbod } from 'app/shared/model/aanbod.model';
import { getEntities as getAanbods } from 'app/entities/aanbod/aanbod.reducer';
import { IActiviteit } from 'app/shared/model/activiteit.model';
import { getEntity, updateEntity, createEntity, reset } from './activiteit.reducer';

export const ActiviteitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const aanbods = useAppSelector(state => state.aanbod.entities);
  const activiteitEntity = useAppSelector(state => state.activiteit.entity);
  const loading = useAppSelector(state => state.activiteit.loading);
  const updating = useAppSelector(state => state.activiteit.updating);
  const updateSuccess = useAppSelector(state => state.activiteit.updateSuccess);

  const handleClose = () => {
    navigate('/activiteit');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAanbods({}));
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
    if (values.afhandeltermijn !== undefined && typeof values.afhandeltermijn !== 'number') {
      values.afhandeltermijn = Number(values.afhandeltermijn);
    }

    const entity = {
      ...activiteitEntity,
      ...values,
      aanbods: mapIdList(values.aanbods),
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
          ...activiteitEntity,
          aanbods: activiteitEntity?.aanbods?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.activiteit.home.createOrEditLabel" data-cy="ActiviteitCreateUpdateHeading">
            Create or edit a Activiteit
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="activiteit-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Code" id="activiteit-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Naam" id="activiteit-naam" name="naam" data-cy="naam" type="text" />
              <ValidatedField label="Actiehouder" id="activiteit-actiehouder" name="actiehouder" data-cy="actiehouder" type="text" />
              <ValidatedField
                label="Afhandeltermijn"
                id="activiteit-afhandeltermijn"
                name="afhandeltermijn"
                data-cy="afhandeltermijn"
                type="text"
              />
              <ValidatedField label="Actief" id="activiteit-actief" name="actief" data-cy="actief" check type="checkbox" />
              <ValidatedField label="Aanbod" id="activiteit-aanbod" data-cy="aanbod" type="select" multiple name="aanbods">
                <option value="" key="0" />
                {aanbods
                  ? aanbods.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activiteit" replace color="info">
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

export default ActiviteitUpdate;
