import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IInwonerplan } from 'app/shared/model/inwonerplan.model';
import { getEntities as getInwonerplans } from 'app/entities/inwonerplan/inwonerplan.reducer';
import { IInwonerprofiel } from 'app/shared/model/inwonerprofiel.model';
import { getEntity, updateEntity, createEntity, reset } from './inwonerprofiel.reducer';

export const InwonerprofielUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const inwonerplans = useAppSelector(state => state.inwonerplan.entities);
  const inwonerprofielEntity = useAppSelector(state => state.inwonerprofiel.entity);
  const loading = useAppSelector(state => state.inwonerprofiel.loading);
  const updating = useAppSelector(state => state.inwonerprofiel.updating);
  const updateSuccess = useAppSelector(state => state.inwonerprofiel.updateSuccess);

  const handleClose = () => {
    navigate('/inwonerprofiel');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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

    const entity = {
      ...inwonerprofielEntity,
      ...values,
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
          ...inwonerprofielEntity,
          inwonerplan: inwonerprofielEntity?.inwonerplan?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.inwonerprofiel.home.createOrEditLabel" data-cy="InwonerprofielCreateUpdateHeading">
            Create or edit a Inwonerprofiel
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
                <ValidatedField name="id" required readOnly id="inwonerprofiel-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Voornaam"
                id="inwonerprofiel-voornaam"
                name="voornaam"
                data-cy="voornaam"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Tussenvoegsel"
                id="inwonerprofiel-tussenvoegsel"
                name="tussenvoegsel"
                data-cy="tussenvoegsel"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Achternaam"
                id="inwonerprofiel-achternaam"
                name="achternaam"
                data-cy="achternaam"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Geboortedatum"
                id="inwonerprofiel-geboortedatum"
                name="geboortedatum"
                data-cy="geboortedatum"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Bsn"
                id="inwonerprofiel-bsn"
                name="bsn"
                data-cy="bsn"
                type="text"
                validate={{
                  pattern: { value: /\b[0-9]{9}\b/, message: translate('entity.validation.pattern', { pattern: '\\b[0-9]{9}\\b' }) },
                }}
              />
              <ValidatedField id="inwonerprofiel-inwonerplan" name="inwonerplan" data-cy="inwonerplan" label="Inwonerplan" type="select">
                <option value="" key="0" />
                {inwonerplans
                  ? inwonerplans.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/inwonerprofiel" replace color="info">
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

export default InwonerprofielUpdate;
