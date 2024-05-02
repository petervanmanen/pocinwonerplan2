import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInwonerplanSubDoel } from 'app/shared/model/inwonerplan-sub-doel.model';
import { getEntities as getInwonerplanSubDoels } from 'app/entities/inwonerplan-sub-doel/inwonerplan-sub-doel.reducer';
import { IInwonerplanActiviteit } from 'app/shared/model/inwonerplan-activiteit.model';
import { ActiviteitStatus } from 'app/shared/model/enumerations/activiteit-status.model';
import { getEntity, updateEntity, createEntity, reset } from './inwonerplan-activiteit.reducer';

export const InwonerplanActiviteitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const inwonerplanSubDoels = useAppSelector(state => state.inwonerplanSubDoel.entities);
  const inwonerplanActiviteitEntity = useAppSelector(state => state.inwonerplanActiviteit.entity);
  const loading = useAppSelector(state => state.inwonerplanActiviteit.loading);
  const updating = useAppSelector(state => state.inwonerplanActiviteit.updating);
  const updateSuccess = useAppSelector(state => state.inwonerplanActiviteit.updateSuccess);
  const activiteitStatusValues = Object.keys(ActiviteitStatus);

  const handleClose = () => {
    navigate('/inwonerplan-activiteit');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getInwonerplanSubDoels({}));
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
      ...inwonerplanActiviteitEntity,
      ...values,
      inwonerplanSubDoel: inwonerplanSubDoels.find(it => it.id.toString() === values.inwonerplanSubDoel?.toString()),
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
          status: 'OPEN',
          ...inwonerplanActiviteitEntity,
          inwonerplanSubDoel: inwonerplanActiviteitEntity?.inwonerplanSubDoel?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.inwonerplanActiviteit.home.createOrEditLabel" data-cy="InwonerplanActiviteitCreateUpdateHeading">
            Create or edit a Inwonerplan Activiteit
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
                <ValidatedField name="id" required readOnly id="inwonerplan-activiteit-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Actiehouder"
                id="inwonerplan-activiteit-actiehouder"
                name="actiehouder"
                data-cy="actiehouder"
                type="text"
              />
              <ValidatedField
                label="Begindatum"
                id="inwonerplan-activiteit-begindatum"
                name="begindatum"
                data-cy="begindatum"
                type="date"
              />
              <ValidatedField label="Einddatum" id="inwonerplan-activiteit-einddatum" name="einddatum" data-cy="einddatum" type="date" />
              <ValidatedField label="Naam" id="inwonerplan-activiteit-naam" name="naam" data-cy="naam" type="text" />
              <ValidatedField label="Status" id="inwonerplan-activiteit-status" name="status" data-cy="status" type="select">
                {activiteitStatusValues.map(activiteitStatus => (
                  <option value={activiteitStatus} key={activiteitStatus}>
                    {activiteitStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="inwonerplan-activiteit-inwonerplanSubDoel"
                name="inwonerplanSubDoel"
                data-cy="inwonerplanSubDoel"
                label="Inwonerplan Sub Doel"
                type="select"
              >
                <option value="" key="0" />
                {inwonerplanSubDoels
                  ? inwonerplanSubDoels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/inwonerplan-activiteit" replace color="info">
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

export default InwonerplanActiviteitUpdate;
